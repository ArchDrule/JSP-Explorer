package org.example.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@WebServlet("/explorer")
public class FileExplorerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPath = req.getParameter("path");
        if (currentPath != null && !currentPath.trim().isEmpty()) {
            currentPath = URLDecoder.decode(currentPath, StandardCharsets.UTF_8);
        } else {
            currentPath = System.getProperty("user.dir");
        }

        File currentDir = new File(currentPath);

        if (!currentDir.exists() || !currentDir.isDirectory()) {
            req.setAttribute("error", "Неверный путь или не директория: " + currentPath);
            req.getRequestDispatcher("/fileList.jsp").forward(req, resp);
            return;
        }

        File[] files = currentDir.listFiles();
        if (files == null) files = new File[0];

        req.setAttribute("currentPath", currentDir.getAbsolutePath());
        req.setAttribute("files", files);
        req.setAttribute("parentPath", currentDir.getParent()); // для "Вверх"
        req.setAttribute("serverTime", new Date());

        req.getRequestDispatcher("/fileList.jsp").forward(req, resp);
    }
}
