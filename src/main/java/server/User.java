package server;

public class User {
    private final String username;
    private String name;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
