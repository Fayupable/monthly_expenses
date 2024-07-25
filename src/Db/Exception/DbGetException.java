package Db.Exception;

public class DbGetException extends Exception {
    public DbGetException() {
        super();
    }

    public DbGetException(String message) {
        super(message);
    }

    public DbGetException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbGetException(Throwable cause) {
        super(cause);
    }
}
