package Db.Exception;

public class DbGetException extends Exception {
    private static final String DEFAULT_MESSAGE = "Database get method error";

    public DbGetException() {
        super(DEFAULT_MESSAGE);
    }

    public DbGetException(String message) {
        super(message);
    }

    public DbGetException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbGetException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

}
