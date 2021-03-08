package server;

public class User {
    private final String username;
    private String name;
    private boolean online;

    public User(String username) {
        this.username = username;
        this.online = false;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return online;
    }

    public void setToOnline() {
        online = true;
    }

    public void setToOffline() {
        online = false;
    }
}
