package Db.Exception;

public class DbUpdateException extends Exception {
    private static final String DEFAULT_MESSAGE = "Database update error";

    public DbUpdateException() {
        super(DEFAULT_MESSAGE);
    }

    public DbUpdateException(String message) {
        super(message);
    }

    public DbUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbUpdateException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}