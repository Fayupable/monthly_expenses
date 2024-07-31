package Db;

import Bst.AvlTree;
import Db.Enum.EPaymentMethods;
import Db.Enum.EPersonType;
import Db.Exception.*;
import Db.Tables.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbFunction implements IDbFunction {
    private Connection conn;
    private ResultSet rs;
    private PreparedStatement ps;


    //Login
    private static final String login_query = "SELECT * FROM persons WHERE e_mail = ? AND password = ?";

    //Statistics
    private static final String get_total_expenses_query = "SELECT SUM(cost) FROM expenses WHERE person_id = ?";
    private static final String get_avarage_expenses_query = "SELECT AVG(cost) FROM expenses WHERE person_id = ?";
    private static final String get_max_expenses_query = "SELECT MAX(cost) FROM expenses WHERE person_id = ?";
    private static final String get_min_expenses_query = "SELECT MIN(cost) FROM expenses WHERE person_id = ?";
    private static final String get_between_recent_oldest_expenses_query= "SELECT MIN(date) AS oldest_date, MAX(date) AS newest_date FROM expenses WHERE person_id = ?";


    //Persons
    private static final String insert_person_query = "INSERT INTO persons (name, e_mail, password, person_type, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String get_persons_by_id_query = "SELECT * FROM persons WHERE id = ?";
    private static final String update_person_query = "UPDATE persons SET name = ?, e_mail = ?, password = ? WHERE id = ?";

    //Payment Methods
    private static final String insert_payment_method_query = "INSERT INTO payment_methods (name) VALUES (?)";
    private static final String update_payment_method_query = "UPDATE payment_methods SET name = ? WHERE id = ?";
    private static final String delete_payment_method_query = "DELETE FROM payment_methods WHERE id = ?";
    private static final String get_payment_methods_query = "SELECT * FROM payment_methods";
    private static final String search_payment_methods_query = "SELECT * FROM payment_methods WHERE name LIKE ?";


    //Categories
    private static final String insert_category_query = "INSERT INTO categories (name) VALUES (?)";
    private static final String update_category_query = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String delete_category_query = "DELETE FROM categories WHERE id = ?";
    private static final String get_categories_query = "SELECT * FROM categories";
    private static final String search_categories_query = "SELECT * FROM categories WHERE name LIKE ?";


    //Expenses
    private static final String insert_expense_query = "INSERT INTO expenses (person_id, cost, amount, description, category_id, payment_method_id,date) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String update_expense_query = "UPDATE expenses SET cost = ?, amount = ?, description = ?, category_id = ?, payment_method_id = ?, date = ? WHERE id = ?";
    private static final String delete_expense_query = "DELETE FROM expenses WHERE id = ?";
    private static final String get_expenses_query = "SELECT * FROM expenses";
    private static final String get_expenses_by_person_id_query = "SELECT * FROM expenses WHERE person_id = ?";
    private static final String search_expenses_query = "SELECT * FROM expenses WHERE description LIKE ? OR cost LIKE ? OR amount LIKE ? OR `date` LIKE ? OR category_id LIKE ? OR payment_method_id LIKE ?";
    private static final String filter_asc_expenses_query = "SELECT * FROM expenses ORDER BY cost ASC";
    private static final String filter_desc_expenses_query = "SELECT * FROM expenses ORDER BY cost DESC";
    private static final String filter_date_expenses_query = "SELECT * FROM expenses ORDER BY date";


    @Override
    public int login(Persons person) throws DbConnectException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbConnector.getConnection();
            String login_query = "SELECT id, name, e_mail, password, person_type, created_at FROM persons WHERE e_mail = ? AND password = ?";
            ps = conn.prepareStatement(login_query);
            ps.setString(1, person.getE_mail());
            ps.setString(2, person.getPassword());
            rs = ps.executeQuery();

            if (rs.next()) {
                person.setId(rs.getInt("id"));
                person.setName(rs.getString("name"));
                person.setE_mail(rs.getString("e_mail"));
                person.setPassword(rs.getString("password"));
                person.setPerson_type(EPersonType.valueOf(rs.getString("person_type")));
                person.setCreated_at(rs.getTimestamp("created_at"));
                return person.getId();
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    @Override
    public List<Expenses> getStatistics(int personId) throws DbConnectException, SQLException {
        List<Expenses> expensesList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbConnector.getConnection();

            // Get between recent and oldest date
            ps = conn.prepareStatement(get_between_recent_oldest_expenses_query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()) {
                java.sql.Date oldestDate = rs.getDate("oldest_date");
                java.sql.Date newestDate = rs.getDate("newest_date");

                //write a null? i will fix it
                Expenses dateRangeExpenses = new Expenses();
                dateRangeExpenses.setDescription("between: " + oldestDate + " and " + newestDate);
                expensesList.add(dateRangeExpenses);
            }



            // Get total expenses
            ps = conn.prepareStatement(get_total_expenses_query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Expenses totalExpenses = new Expenses();
                totalExpenses.setDescription("Total Expenses");
                totalExpenses.setCost(rs.getBigDecimal(1));
                expensesList.add(totalExpenses);
            }
            rs.close();
            ps.close();

            // Get average expenses
            ps = conn.prepareStatement(get_avarage_expenses_query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Expenses avgExpenses = new Expenses();
                avgExpenses.setDescription("Average Expenses");
                avgExpenses.setCost(rs.getBigDecimal(1));
                expensesList.add(avgExpenses);
            }
            rs.close();
            ps.close();

            // Get max expenses
            ps = conn.prepareStatement(get_max_expenses_query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Expenses maxExpenses = new Expenses();
                maxExpenses.setDescription("Max Expenses");
                maxExpenses.setCost(rs.getBigDecimal(1));
                expensesList.add(maxExpenses);
            }
            rs.close();
            ps.close();

            // Get min expenses
            ps = conn.prepareStatement(get_min_expenses_query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Expenses minExpenses = new Expenses();
                minExpenses.setDescription("Min Expenses");
                minExpenses.setCost(rs.getBigDecimal(1));
                expensesList.add(minExpenses);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new DbConnectException("Failed to get statistics: " + e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return expensesList;
    }
    //Persons
    @Override
    public void insertPerson(Persons person) throws DbConnectException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(insert_person_query);
        ps.setString(1, person.getName());
        ps.setString(2, person.getE_mail());
        ps.setString(3, person.getPassword());
        ps.setString(4, String.valueOf(person.getPerson_type()));
        ps.setTimestamp(5, person.getCreated_at());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Persons getPersonsById(int id) throws DbConnectException, SQLException {
        Persons person = new Persons();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(get_persons_by_id_query);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        if (rs.next()) {
            person.setId(rs.getInt("id"));
            person.setName(rs.getString("name"));
            person.setE_mail(rs.getString("e_mail"));
            person.setPassword(rs.getString("password"));
            person.setPerson_type(EPersonType.valueOf(rs.getString("person_type")));
            person.setCreated_at(rs.getTimestamp("created_at"));
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return person;

    }

    @Override
    public boolean updatePerson(Persons person) throws DbConnectException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(update_person_query);
        ps.setString(1, person.getName());
        ps.setString(2, person.getE_mail());
        ps.setString(3, person.getPassword());
        ps.setInt(4, person.getId());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void insertPaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(insert_payment_method_query);
        ps.setString(1, String.valueOf(paymentMethods.getPaymentMethod()));
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void updatePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(update_payment_method_query);
        ps.setString(1, String.valueOf(paymentMethods.getPaymentMethod()));
        ps.setInt(2, paymentMethods.getId());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void deletePaymentMethod(PaymentMethods paymentMethods) throws DbConnectException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(delete_payment_method_query);
        ps.setInt(1, paymentMethods.getId());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<PaymentMethods> getPaymentMethods() throws DbConnectException, SQLException {
        List<PaymentMethods> paymentMethods = new ArrayList<>();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(get_payment_methods_query);
        rs = ps.executeQuery();
        while (rs.next()) {
            PaymentMethods paymentMethod = new PaymentMethods();
            paymentMethod.setId(rs.getInt("id"));
            paymentMethod.setPaymentMethod(EPaymentMethods.fromDisplayName(rs.getString("name")));
            paymentMethods.add(paymentMethod);
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return paymentMethods;
    }

    @Override
    public List<PaymentMethods> searchPaymentMethods(String search) throws DbConnectException, SQLException {
        List<PaymentMethods> paymentMethods = new ArrayList<>();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(search_payment_methods_query);
        ps.setString(1, "%" + search + "%");
        rs = ps.executeQuery();
        while (rs.next()) {
            PaymentMethods paymentMethod = new PaymentMethods();
            paymentMethod.setId(rs.getInt("id"));
            paymentMethod.setPaymentMethod(EPaymentMethods.valueOf(rs.getString("name")));
            paymentMethods.add(paymentMethod);
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return paymentMethods;
    }

    //Categories
    @Override
    public void insertCategory(Categories categories) throws DbConnectException, DbInsertException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(insert_category_query);
        ps.setString(1, categories.getName());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateCategory(Categories categories) throws DbConnectException, DbUpdateException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(update_category_query);
        ps.setString(1, categories.getName());
        ps.setInt(2, categories.getId());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void deleteCategory(Categories categories) throws DbConnectException, DbDeleteException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(delete_category_query);
        ps.setInt(1, categories.getId());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<Categories> getCategories() throws DbConnectException, DbSelectException, SQLException {
        List<Categories> categories = new ArrayList<>();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(get_categories_query);
        rs = ps.executeQuery();
        while (rs.next()) {
            Categories category = new Categories();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            categories.add(category);
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return categories;
    }

    @Override
    public List<Categories> searchCategories(String search) throws DbConnectException, DbSearchException, SQLException {
        List<Categories> categories = new ArrayList<>();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(search_categories_query);
        ps.setString(1, "%" + search + "%");
        rs = ps.executeQuery();
        while (rs.next()) {
            Categories category = new Categories();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            categories.add(category);
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return categories;
    }


    //Expenses

    @Override
    public void insertExpense(Expenses expenses) throws DbConnectException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbConnector.getConnection();
            ps = conn.prepareStatement(insert_expense_query);
            ps.setInt(1, expenses.getPerson_id());
            ps.setBigDecimal(2, expenses.getCost());
            ps.setBigDecimal(3, expenses.getAmount());
            ps.setString(4, expenses.getDescription());
            ps.setInt(5, expenses.getCategory_id());
            ps.setInt(6, expenses.getPayment_method_id());
            ps.setDate(7, (Date) expenses.getDate());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbConnectException("Failed to insert expense: " + e.getMessage(), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateExpense(Expenses expenses) throws DbConnectException, SQLException {

        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(update_expense_query);
        ps.setBigDecimal(1, expenses.getCost());
        ps.setBigDecimal(2, expenses.getAmount());
        ps.setString(3, expenses.getDescription());
        ps.setInt(4, expenses.getCategory_id());
        ps.setInt(5, expenses.getPayment_method_id());
        ps.setDate(6, (Date) expenses.getDate());
        ps.setInt(7, expenses.getId());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteExpense(Expenses expenses) throws DbConnectException, SQLException {
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(delete_expense_query);
        ps.setInt(1, expenses.getId());
        ps.executeUpdate();
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Expenses> getExpenses() throws DbConnectException, SQLException {
        List<Expenses> expenses = new ArrayList<>();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(get_expenses_query);
        rs = ps.executeQuery();
        while (rs.next()) {
            Expenses expense = new Expenses();
            expense.setId(rs.getInt("id"));
            expense.setPerson_id(rs.getInt("person_id"));
            expense.setCost(rs.getBigDecimal("cost"));
            expense.setAmount(rs.getBigDecimal("amount"));
            expense.setDescription(rs.getString("description"));
            expense.setCategory_id(rs.getInt("category_id"));
            expense.setPayment_method_id(rs.getInt("payment_method_id"));
            expense.setDate(rs.getDate("date"));
            expenses.add(expense);
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return expenses;
    }

    @Override
    public List<Expenses> getExpensesByPersonId(int personId) throws DbConnectException, SQLException {
        List<Expenses> expenses = new ArrayList<>();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(get_expenses_by_person_id_query);
        ps.setInt(1, personId);
        rs = ps.executeQuery();
        while (rs.next()) {
            Expenses expense = new Expenses();
            expense.setId(rs.getInt("id"));
            expense.setPerson_id(rs.getInt("person_id"));
            expense.setCost(rs.getBigDecimal("cost"));
            expense.setAmount(rs.getBigDecimal("amount"));
            expense.setDescription(rs.getString("description"));
            expense.setCategory_id(rs.getInt("category_id"));
            expense.setPayment_method_id(rs.getInt("payment_method_id"));
            expense.setDate(rs.getDate("date"));
            expenses.add(expense);
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return expenses;


    }

    @Override
    public List<Expenses> searchExpenses(String search) throws DbConnectException, SQLException {
        List<Expenses> expenses = new ArrayList<>();
        conn = DbConnector.getConnection();
        ps = conn.prepareStatement(search_expenses_query);
        ps.setString(1, "%" + search + "%");
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");
        ps.setString(4, "%" + search + "%");
        ps.setString(5, "%" + search + "%");
        ps.setString(6, "%" + search + "%");

        rs = ps.executeQuery();
        while (rs.next()) {
            Expenses expense = new Expenses();
            expense.setId(rs.getInt("id"));
            expense.setPerson_id(rs.getInt("person_id"));
            expense.setCost(rs.getBigDecimal("cost"));
            expense.setAmount(rs.getBigDecimal("amount"));
            expense.setDescription(rs.getString("description"));
            expense.setCategory_id(rs.getInt("category_id"));
            expense.setPayment_method_id(rs.getInt("payment_method_id"));
            expense.setDate(rs.getDate("date"));
            expenses.add(expense);
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return expenses;

    }

    public List<Expenses> getExpensesSorted(String sortOrder) {
        String query;
        switch (sortOrder) {
            case "asc":
                query = filter_asc_expenses_query;
                break;
            case "desc":
                query = filter_desc_expenses_query;
                break;
            case "date":
                query = filter_date_expenses_query;
                break;
            default:
                throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
        }

        List<Expenses> expenses = new ArrayList<>();

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Expenses expense = new Expenses();
                expense.setId(rs.getInt("id"));
                expense.setPerson_id(rs.getInt("person_id"));
                expense.setCost(rs.getBigDecimal("cost"));
                expense.setAmount(rs.getBigDecimal("amount"));
                expense.setDescription(rs.getString("description"));
                expense.setCategory_id(rs.getInt("category_id"));
                expense.setPayment_method_id(rs.getInt("payment_method_id"));
                expense.setDate(rs.getDate("date"));
                expenses.add(expense);
            }
        } catch (SQLException | DbConnectException e) {
            e.printStackTrace();
        }

        return expenses;
    }

}
