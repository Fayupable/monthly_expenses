package Db;

import Db.Exception.DbConnectException;
import Db.Tables.Persons;

import java.sql.SQLException;

public interface IDbFunction {
    boolean login(Persons person) throws DbConnectException, SQLException;


}
