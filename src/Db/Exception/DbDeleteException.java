package Db.Exception;

public class DbDeleteException extends Exception {
    private static final String DEFAULT_MESSAGE = "Database delete error";

    public DbDeleteException() {
        super(DEFAULT_MESSAGE);
    }

    public DbDeleteException(String message) {
        super(message);
    }

    public DbDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbDeleteException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

}