package Db.Exception;

public class DbInsertException extends Exception {
    public DbInsertException() {
        super();
    }

    public DbInsertException(String message) {
        super(message);
    }

    public DbInsertException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbInsertException(Throwable cause) {
        super(cause);
    }
}