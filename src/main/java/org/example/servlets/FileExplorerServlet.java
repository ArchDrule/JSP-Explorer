package org.example.servlets;

import accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@WebServlet("/explorer")
public class FileExplorerServlet extends HttpServlet {
    private static final String BASE_USERS_DIR = System.getProperty("user.home") + File.separator + "filemanager";

    @Override
    public void init() {
        File baseDir = new File(BASE_USERS_DIR);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserProfile user = (session != null) ? (UserProfile) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        String userHome = BASE_USERS_DIR + File.separator + user.getLogin();
        File userHomeDir = new File(userHome);
        if (!userHomeDir.exists()) {
            userHomeDir.mkdirs();
        }

        String currentPath = req.getParameter("path");
        if (currentPath != null && !currentPath.trim().isEmpty()) {
            currentPath = URLDecoder.decode(currentPath, StandardCharsets.UTF_8);
        } else {
            currentPath = userHome;
        }

        File currentDir = new File(currentPath);

        // Проверка, что путь внутри домашней папки пользователя
        try {
            String canonicalUserHome = userHomeDir.getCanonicalPath();
            String canonicalCurrent = currentDir.getCanonicalPath();
            if (!canonicalCurrent.startsWith(canonicalUserHome)) {
                req.setAttribute("error", "Доступ запрещен: выход за пределы домашней папки");
                req.getRequestDispatcher("/WEB-INF/fileList.jsp").forward(req, resp);
                return;
            }
        } catch (IOException e) {
            req.setAttribute("error", "Ошибка доступа к файловой системе");
            req.getRequestDispatcher("/WEB-INF/fileList.jsp").forward(req, resp);
            return;
        }

        if (!currentDir.exists() || !currentDir.isDirectory()) {
            req.setAttribute("error", "Неверный путь или не директория: " + currentPath);
            req.getRequestDispatcher("/WEB-INF/fileList.jsp").forward(req, resp);
            return;
        }

        File[] files = currentDir.listFiles();
        if (files == null) files = new File[0];

        req.setAttribute("currentPath", currentDir.getAbsolutePath());
        req.setAttribute("files", files);
        req.setAttribute("parentPath", currentDir.getParent());
        req.setAttribute("serverTime", new Date());
        req.setAttribute("userLogin", user.getLogin());

        req.getRequestDispatcher("/WEB-INF/fileList.jsp").forward(req, resp);
    }
}
