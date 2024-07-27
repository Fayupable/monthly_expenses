package Db;

import Db.Enum.EPaymentMethods;
import Db.Exception.*;
import Db.Tables.Categories;
import Db.Tables.Expenses;
import Db.Tables.PaymentMethods;
import Db.Tables.Persons;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbFunction implements IDbFunction {
    private Connection conn;
    private ResultSet rs;
    private PreparedStatement ps;


    //Login
    private static final String login_query = "SELECT * FROM persons WHERE e_mail = ? AND password = ?";

    //Persons
    private static final String insert_person_query = "INSERT INTO persons (name, e_mail, password, person_type, created_at) VALUES (?, ?, ?, ?, ?)";

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
    private static final String update_expense_query = "UPDATE expenses SET person_id = ?, cost = ?, amount = ?, description = ?, category_id = ?, payment_method_id = ?, date = ? WHERE id = ?";
    private static final String delete_expense_query = "DELETE FROM expenses WHERE id = ?";
    private static final String get_expenses_query = "SELECT * FROM expenses";
    private static final String search_expenses_query = "SELECT * FROM expenses WHERE description LIKE ?";


    //Expenses Details

    @Override
    public boolean login(Persons person) throws DbConnectException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbConnector.getConnection();
            ps = conn.prepareStatement(login_query);
            ps.setString(1, person.getE_mail());
            ps.setString(2, person.getPassword());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
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
        return false;
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

    @Override
    public void insertExpense(Expenses expenses) throws DbConnectException, SQLException {
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
    public void updateExpense(Expenses expenses) throws DbConnectException, SQLException {

    }

    @Override
    public void deleteExpense(Expenses expenses) throws DbConnectException, SQLException {

    }

    @Override
    public List<Expenses> getExpenses() throws DbConnectException, SQLException {
        return List.of();
    }

    @Override
    public List<Expenses> searchExpenses(String search) throws DbConnectException, SQLException {
        return List.of();
    }
}
