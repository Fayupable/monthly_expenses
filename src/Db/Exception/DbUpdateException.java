package Db.Exception;

public class DbUpdateException extends Exception {
    public DbUpdateException() {
        super();
    }

    public DbUpdateException(String message) {
        super(message);
    }

    public DbUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbUpdateException(Throwable cause) {
        super(cause);
    }
}