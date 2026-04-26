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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private AccountService accountService;

    @Override
    public void init() {
        accountService = (AccountService) getServletContext().getAttribute("accountService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        String email = req.getParameter("email");

        if (login == null || pass == null || email == null ||
                login.trim().isEmpty() || pass.trim().isEmpty() || email.trim().isEmpty()) {
            req.setAttribute("error", "Все поля обязательны");
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
            return;
        }

        if (accountService.getUserByLogin(login) != null) {
            req.setAttribute("error", "Пользователь с таким логином уже существует");
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
            return;
        }

        accountService.addNewUser(new UserProfile(login, pass, email));

        HttpSession session = req.getSession();
        session.setAttribute("user", accountService.getUserByLogin(login));

        resp.sendRedirect("explorer");
    }
}