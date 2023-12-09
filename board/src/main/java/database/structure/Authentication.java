package database.structure;

public class Authentication {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String USERNAME_REGEX = "^[A-Za-z0-9_-]{3,15}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean register(String username, String email, String password) {
        if (!username.matches(USERNAME_REGEX)) {
            return false;
        }
        if (!email.matches(EMAIL_REGEX)) {
            return false;
        }
        if (!password.matches(PASSWORD_REGEX)) {
            return false;
        }

        try {
            Database db = Database.getInstance();
            db.createUserAndCredentials(username, email, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean login(String username, String password) {
        try {
            Database db = Database.getInstance();
            return db.authenticate(username, password);
        } catch (Exception e) {
            return false;
        }
    }
}
