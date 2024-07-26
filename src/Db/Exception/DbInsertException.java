package Db.Exception;

public class DbInsertException extends Exception {
    private static final String DEFAULT_MESSAGE = "Insert operation failed.";

    public DbInsertException() {
        super(DEFAULT_MESSAGE);
    }

    public DbInsertException(String message) {
        super(message);
    }

    public DbInsertException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbInsertException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }


}