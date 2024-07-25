package Db;

import Db.Exception.DbConnectException;
import Db.Tables.Persons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbFunction implements IDbFunction {
    private Connection conn;
    private ResultSet rs;
    private PreparedStatement ps;


    private static final String login_query = "SELECT * FROM persons WHERE e_mail = ? AND password = ?";

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

}
