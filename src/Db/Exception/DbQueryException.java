package Db.Exception;

public class DbQueryException extends Exception {
    private static final String DEFAULT_MESSAGE = "An error occurred while querying the database.";

    public DbQueryException() {
        super(DEFAULT_MESSAGE);
    }

    public DbQueryException(String message) {
        super(message);
    }

    public DbQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbQueryException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
    
}