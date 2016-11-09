package game.user;

public class LoggedInUser {

    private String username;

    private static LoggedInUser instance = new LoggedInUser();

    public static void setLoggedInUser(String newUsername) {
        instance.setUsername(newUsername);
    }

    private void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public static String getLoggedInUser() {
        return instance.getUsername();
    }


    private String getUsername() {
        return instance.username;
    }

}
