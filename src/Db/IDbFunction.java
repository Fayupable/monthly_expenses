package Db;

import Db.Exception.*;
import Db.Tables.Categories;
import Db.Tables.PaymentMethods;
import Db.Tables.Persons;

import java.sql.SQLException;
import java.util.List;

public interface IDbFunction {
    //Persons
    boolean login(Persons person) throws DbConnectException, SQLException;
    void insertPerson(Persons person) throws DbConnectException, SQLException;



    //Categories
    void insertCategory(Categories categories) throws DbConnectException, DbInsertException,SQLException;
    void updateCategory(Categories categories) throws DbConnectException, DbUpdateException,SQLException;
    void deleteCategory(Categories categories) throws DbConnectException, DbDeleteException,SQLException;
    List<Categories> getCategories() throws DbConnectException, DbSelectException,SQLException;
    List<Categories> searchCategories(String search) throws DbConnectException,DbSearchException ,SQLException;



    //Expenses



    //Expenses Details


    //Payment Methods
    void insertPaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;
    void updatePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;
    void deletePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;
    List<PaymentMethods> getPaymentMethods() throws DbConnectException, SQLException;
    List<PaymentMethods> searchPaymentMethods(String search) throws DbConnectException, SQLException;









}
