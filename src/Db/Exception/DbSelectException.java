package Db.Exception;

public class DbSelectException extends Exception {
    public DbSelectException() {
        super();
    }

    public DbSelectException(String message) {
        super(message);
    }

    public DbSelectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbSelectException(Throwable cause) {
        super(cause);
    }
}