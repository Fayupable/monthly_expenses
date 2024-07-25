package Db.Exception;

public class DbSearchException extends Exception {
    public DbSearchException() {
        super();
    }

    public DbSearchException(String message) {
        super(message);
    }

    public DbSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbSearchException(Throwable cause) {
        super(cause);
    }
}
