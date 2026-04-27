package dbService;

import accounts.UserProfile;
import dbService.dao.UsersDAO;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private final Connection connection;

    public DBService() {
        this.connection = getMysqlConnection();
    }

    public void createUsersTable() throws DBException {
        try {
            UsersDAO dao = new UsersDAO(connection);
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void addUser(UserProfile userProfile) throws DBException {
        try {
            connection.setAutoCommit(false);
            UsersDAO dao = new UsersDAO(connection);
            dao.insertUser(userProfile);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public UserProfile getUserByLogin(String login) throws DBException {
        try {
            UsersDAO dao = new UsersDAO(connection);
            return dao.getUserByLogin(login);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getMysqlConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/jsp_explorer" +
                    "?user=root" +
                    "&password=12345" +
                    "&useSSL=false" +
                    "&allowPublicKeyRetrieval=true" +
                    "&serverTimezone=UTC" +
                    "&characterEncoding=utf8" +
                    "&useUnicode=true";

            System.out.println("Подключение к БД...");
            System.out.println("URL: " + url.replace("12345", "***"));

            Connection connection = DriverManager.getConnection(url);
            System.out.println("Подключение к MySQL успешно установлено!");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("Драйвер MySQL не найден: " + e.getMessage());
            throw new RuntimeException("Драйвер MySQL не найден", e);
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к MySQL: " + e.getMessage());
            System.err.println("Код ошибки SQL: " + e.getErrorCode());
            System.err.println("SQL состояние: " + e.getSQLState());
            throw new RuntimeException("Не удалось подключиться к MySQL", e);
        }
    }
}