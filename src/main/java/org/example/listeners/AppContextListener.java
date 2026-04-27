package org.example.listeners;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());
    private DBService dbService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            dbService = new DBService();
            dbService.createUsersTable();

            AccountService accountService = new AccountService(dbService);
            ServletContext context = sce.getServletContext();
            context.setAttribute("accountService", accountService);

            if (accountService.getUserByLogin("admin") == null) {
                UserProfile admin = new UserProfile("admin", "admin", "admin@example.com");
                accountService.addNewUser(admin);
                LOGGER.info("Создан тестовый пользователь admin/admin");
            }

            LOGGER.info("Приложение инициализировано. Подключение к БД установлено.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Критическая ошибка при инициализации приложения", e);
            throw new RuntimeException("Не удалось инициализировать приложение", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dbService != null) {
            dbService.close();
            LOGGER.info("Подключение к БД закрыто");
        }
    }
}