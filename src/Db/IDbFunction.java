package Db;

import Db.Exception.*;
import Db.Tables.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IDbFunction {
    //Persons
    int login(Persons person) throws DbConnectException, SQLException;

    void insertPerson(Persons person) throws DbConnectException, SQLException;

    Persons getPersonsById(int id) throws DbConnectException, SQLException;

    boolean updatePerson(Persons person) throws DbConnectException, SQLException;


    //Categories
    void insertCategory(Categories categories) throws DbConnectException, DbInsertException, SQLException;

    void updateCategory(Categories categories) throws DbConnectException, DbUpdateException, SQLException;

    void deleteCategory(Categories categories) throws DbConnectException, DbDeleteException, SQLException;

    List<Categories> getCategories() throws DbConnectException, DbSelectException, SQLException;

    List<Categories> searchCategories(String search) throws DbConnectException, DbSearchException, SQLException;


    //Expenses
    void insertExpense(Expenses expenses) throws DbConnectException, SQLException;

    void updateExpense(Expenses expenses) throws DbConnectException, SQLException;

    void deleteExpense(Expenses expenses) throws DbConnectException, SQLException;

    List<Expenses> getExpenses() throws DbConnectException, SQLException;

    List<Expenses> searchExpenses(String search) throws DbConnectException, SQLException;

    List<Expenses> getExpensesByPersonId(int personId) throws DbConnectException, SQLException;

    List<Expenses> getExpensesSorted(String sortOrder) throws DbConnectException, SQLException;


    //Payment Methods
    void insertPaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;

    void updatePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;

    void deletePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException;

    List<PaymentMethods> getPaymentMethods() throws DbConnectException, SQLException;

    List<PaymentMethods> searchPaymentMethods(String search) throws DbConnectException, SQLException;

    //Statistics
    List<Expenses> getStatistics(int personId) throws DbConnectException, SQLException;

    public List<Expenses> getTotalExpenses(int personId) throws DbConnectException, SQLException;

    public List<Expenses> getAvarageExpenses(int personId) throws DbConnectException, SQLException;

    public List<Expenses> getMaxExpenses(int personId) throws DbConnectException, SQLException;

    public List<Expenses> getMinExpenses(int personId) throws DbConnectException, SQLException;

    public List<Expenses> getBetweenRecentOldestExpenses(int personId) throws DbConnectException, SQLException;

    public List<Expenses> getBetweenDateGetDateFromUser(int personId, Date startDate, Date endDate) throws DbConnectException, SQLException;

    public List<Expenses> getStatisticsBetweenDates(int personId, Date startDate, Date endDate) throws DbConnectException, SQLException;

    public List<Expenses> getStatisticsMinExpensesBetweenDates(int personId, Date startDate, Date endDate) throws DbConnectException, SQLException;

    public List<Expenses> getStatisticsMaxExpensesBetweenDates(int personId, Date startDate, Date endDate) throws DbConnectException, SQLException;

    public List<Expenses> getStatisticsAvgExpensesBetweenDates(int personId, Date startDate, Date endDate) throws DbConnectException, SQLException;

    public List<Expenses> getStatisticsTotalExpensesBetweenDates(int personId, Date startDate, Date endDate) throws DbConnectException, SQLException;


}
