package org.example.servlets;

import accounts.AccountService;
import accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AccountService accountService;

    @Override
    public void init() {
        accountService = (AccountService) getServletContext().getAttribute("accountService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Tomcat сам восстановит сессию из cookie, если она есть на диске
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect("explorer");
            return;
        }

        String message = req.getParameter("message");
        if (message != null && !message.isEmpty()) {
            req.setAttribute("message", message);
        }

        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");

        if (login == null || pass == null || login.trim().isEmpty() || pass.trim().isEmpty()) {
            req.setAttribute("error", "Логин и пароль обязательны");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
            return;
        }

        if (accountService.checkCredentials(login, pass)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", accountService.getUserByLogin(login));
            resp.sendRedirect("explorer");
        } else {
            req.setAttribute("error", "Неверный логин или пароль");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        }
    }
}