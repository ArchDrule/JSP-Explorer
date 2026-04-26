package accounts;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String login;
    private String pass;
    private String email;

    public UserProfile() {
    }

    public UserProfile(String login, String pass, String email) {
        this.login = login;
        this.pass = pass;
        this.email = email;
    }

    public String getLogin() { return login; }
    public String getPass() { return pass; }
    public String getEmail() { return email; }

    public void setLogin(String login) { this.login = login; }
    public void setPass(String pass) { this.pass = pass; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "UserProfile{login='" + login + "', email='" + email + "'}";
    }
}