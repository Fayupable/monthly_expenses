package Db.Exception;

public class DbConnectException extends Exception {
    private static final String DEFAULT_MESSAGE = "Database connection error";
    public DbConnectException() {
        super(DEFAULT_MESSAGE);
    }

    public DbConnectException(String message) {
        super(message);
    }

    public DbConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbConnectException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}