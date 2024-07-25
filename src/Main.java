import Db.DbFunction;
import Db.Enum.EPersonType;
import Db.Exception.DbConnectException;
import Db.Tables.Persons;

import java.sql.SQLException;
import java.sql.Timestamp;

public class Main {

    public static void main(String[] args) {
        DbFunction dbFunction = new DbFunction();
        Persons person = new Persons("a", "Ali Veli", "e_mail", EPersonType.MEMBER, Timestamp.from(java.time.Instant.now()));

        try {
            boolean isLoggedIn = dbFunction.login(person);
            System.out.println("Login attempt: " + isLoggedIn);
        } catch (DbConnectException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}