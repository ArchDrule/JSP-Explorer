package org.example.listeners;

import accounts.AccountService;
import accounts.UserProfile;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AccountService accountService = new AccountService();
        ServletContext context = sce.getServletContext();
        context.setAttribute("accountService", accountService);

        // Можно добавить тестового пользователя для проверки
        accountService.addNewUser(new UserProfile("admin", "admin", "admin@example.com"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}