package Db.Exception;

public class DbSelectException extends Exception {
    private static final String DEFAULT_MESSAGE = "An error occurred while selecting from the database.";

    public DbSelectException() {
        super(DEFAULT_MESSAGE);
    }

    public DbSelectException(String message) {
        super(message);
    }

    public DbSelectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbSelectException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }


}