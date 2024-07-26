package Db.Exception;

public class DbSearchException extends Exception {
    private static final String DEFAULT_MESSAGE = "An error occurred while searching the database.";

    public DbSearchException() {
        super(DEFAULT_MESSAGE);
    }

    public DbSearchException(String message) {
        super(message);
    }

    public DbSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbSearchException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }


}
