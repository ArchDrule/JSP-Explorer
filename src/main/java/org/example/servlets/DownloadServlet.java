package org.example.servlets;

import accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    private static final String BASE_USERS_DIR = System.getProperty("user.home") + File.separator + "filemanager";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        UserProfile user = (session != null) ? (UserProfile) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Необходима авторизация");
            return;
        }

        String filePath = req.getParameter("file");
        if (filePath == null || filePath.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Файл не указан");
            return;
        }

        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        File file = new File(filePath);

        String userHome = BASE_USERS_DIR + File.separator + user.getLogin();
        File userHomeDir = new File(userHome);

        try {
            String canonicalUserHome = userHomeDir.getCanonicalPath();
            String canonicalFile = file.getCanonicalPath();
            if (!canonicalFile.startsWith(canonicalUserHome)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
                return;
            }
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка доступа");
            return;
        }

        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден");
            return;
        }

        resp.setContentType("application/octet-stream");

        // Правильная установка Content-Disposition с поддержкой кириллицы
        String fileName = file.getName();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");

        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + encodedFileName + "\"; " +
                        "filename*=UTF-8''" + encodedFileName);

        resp.setContentLengthLong(file.length());

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}