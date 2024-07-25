package Db.Exception;

public class DbTransactionException extends Exception {
    public DbTransactionException() {
        super();
    }

    public DbTransactionException(String message) {
        super(message);
    }

    public DbTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbTransactionException(Throwable cause) {
        super(cause);
    }
}