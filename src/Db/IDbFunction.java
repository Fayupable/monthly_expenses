package Db;

import Db.Exception.*;
import Db.Tables.*;

import java.sql.SQLException;
import java.util.List;

public interface IDbFunction {
    //Persons
    int login(Persons person) throws DbConnectException, SQLException;
    void insertPerson(Persons person) throws DbConnectException, SQLException;
    Persons getPersonsById(int id) throws DbConnectException, SQLException;



    //Categories
    void insertCategory(Categories categories) throws DbConnectException, DbInsertException,SQLException;
    void updateCategory(Categories categories) throws DbConnectException, DbUpdateException,SQLException;
    void deleteCategory(Categories categories) throws DbConnectException, DbDeleteException,SQLException;
    List<Categories> getCategories() throws DbConnectException, DbSelectException,SQLException;
    List<Categories> searchCategories(String search) throws DbConnectException,DbSearchException ,SQLException;



    //Expenses
    void insertExpense(Expenses expenses) throws DbConnectException, SQLException;
    void updateExpense(Expenses expenses) throws DbConnectException, SQLException;
    void deleteExpense(Expenses expenses) throws DbConnectException, SQLException;
    List<Expenses> getExpenses() throws DbConnectException, SQLException;
    List<Expenses> searchExpenses(String search) throws DbConnectException, SQLException;
    List<Expenses> getExpensesByPersonId(int personId) throws DbConnectException, SQLException;
    List<Expenses> getExpensesSorted(String sortOrder) throws DbConnectException, SQLException;




    //Expenses Details
    void updateExpenseDetails(ExpensesDetails expensesDetails) throws DbConnectException, SQLException;
    void deleteExpenseDetails(ExpensesDetails expensesDetails) throws DbConnectException, SQLException;
    List<ExpensesDetails> getExpenseDetails(int expenseId) throws DbConnectException, SQLException;
    List<ExpensesDetails> searchExpenseDetails(String search) throws DbConnectException, SQLException;
    List<ExpensesDetails> getExpenseDetails() throws DbConnectException, SQLException;
    List<ExpensesDetails>  getExpensesDetailsSorted(String sortOrder) throws DbConnectException, SQLException;
    List<ExpensesDetails> getExpensesDetailByPersonId(int personId) throws DbConnectException, SQLException;






    //Payment Methods
    void insertPaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;
    void updatePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;
    void deletePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;
    List<PaymentMethods> getPaymentMethods() throws DbConnectException, SQLException;
    List<PaymentMethods> searchPaymentMethods(String search) throws DbConnectException, SQLException;









}
