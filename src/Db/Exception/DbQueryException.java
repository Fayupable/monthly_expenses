package Db.Exception;

public class DbQueryException extends Exception {
    public DbQueryException() {
        super();
    }

    public DbQueryException(String message) {
        super(message);
    }

    public DbQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbQueryException(Throwable cause) {
        super(cause);
    }
}