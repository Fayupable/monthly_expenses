package Db.Exception;

public class DbConnectException extends Exception {
    public DbConnectException() {
        super();
    }

    public DbConnectException(String message) {
        super(message);
    }

    public DbConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbConnectException(Throwable cause) {
        super(cause);
    }
}