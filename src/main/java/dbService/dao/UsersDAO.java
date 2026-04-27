package dbService.dao;

import accounts.UserProfile;
import dbService.dataSets.UsersDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAO {

    private Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void createTable() throws SQLException {
        executor.execUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id BIGINT AUTO_INCREMENT, " +
                        "login VARCHAR(256) NOT NULL UNIQUE, " +
                        "pass VARCHAR(256) NOT NULL, " +
                        "email VARCHAR(256), " +
                        "PRIMARY KEY (id)" +
                        ")"
        );
    }

    public void insertUser(UserProfile userProfile) throws SQLException {
        String sql = String.format(
                "INSERT INTO users (login, pass, email) VALUES ('%s', '%s', '%s')",
                userProfile.getLogin(),
                userProfile.getPass(),
                userProfile.getEmail()
        );
        executor.execUpdate(sql);
    }

    public UserProfile getUserByLogin(String login) throws SQLException {
        return executor.execQuery(
                "SELECT * FROM users WHERE login='" + login + "'",
                result -> {
                    if (result.next()) {
                        return new UserProfile(
                                result.getString("login"),
                                result.getString("pass"),
                                result.getString("email")
                        );
                    }
                    return null;
                }
        );
    }

    public UsersDataSet getUserById(long id) throws SQLException {
        return executor.execQuery(
                "SELECT * FROM users WHERE id=" + id,
                result -> {
                    if (result.next()) {
                        return new UsersDataSet(
                                result.getLong("id"),
                                result.getString("login"),
                                result.getString("pass"),
                                result.getString("email")
                        );
                    }
                    return null;
                }
        );
    }
}