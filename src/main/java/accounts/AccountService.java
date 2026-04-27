package accounts;

import dbService.DBException;
import dbService.DBService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountService {
    private static final Logger LOGGER = Logger.getLogger(AccountService.class.getName());
    private final DBService dbService;

    public AccountService(DBService dbService) {
        this.dbService = dbService;
    }

    public void addNewUser(UserProfile userProfile) {
        try {
            dbService.addUser(userProfile);
        } catch (DBException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении пользователя: " + userProfile.getLogin(), e);
            throw new RuntimeException("Не удалось создать пользователя", e);
        }
    }

    public UserProfile getUserByLogin(String login) {
        try {
            return dbService.getUserByLogin(login);
        } catch (DBException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении пользователя: " + login, e);
            return null;
        }
    }

    public boolean checkCredentials(String login, String pass) {
        try {
            UserProfile profile = dbService.getUserByLogin(login);
            return profile != null && profile.getPass().equals(pass);
        } catch (DBException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при проверке учетных данных для: " + login, e);
            return false;
        }
    }
}