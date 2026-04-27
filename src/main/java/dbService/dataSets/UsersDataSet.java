package dbService.dataSets;

public class UsersDataSet {
    private long id;
    private String login;
    private String pass;
    private String email;

    public UsersDataSet(long id, String login, String pass, String email) {
        this.id = id;
        this.login = login;
        this.pass = pass;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UsersDataSet{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}