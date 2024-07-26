package Db.Exception;

public class DbTransactionException extends Exception {
    private static final String DEFAULT_MESSAGE = "An error occurred while performing a transaction on the database.";

    public DbTransactionException() {
        super(DEFAULT_MESSAGE);
    }

    public DbTransactionException(String message) {
        super(message);
    }

    public DbTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbTransactionException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

}