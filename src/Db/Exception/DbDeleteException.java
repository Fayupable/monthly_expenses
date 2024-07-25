package Db.Exception;

public class DbDeleteException extends Exception {
    public DbDeleteException() {
        super();
    }

    public DbDeleteException(String message) {
        super(message);
    }

    public DbDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbDeleteException(Throwable cause) {
        super(cause);
    }
}