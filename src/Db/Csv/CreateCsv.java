package Db.Csv;


import Db.DbConnector;
import Db.Exception.DbConnectException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class CreateCsv {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PrintWriter csvWriter;


    private static final String EXPORT_EXPENSES_QUERY = "SELECT * FROM expenses";



    public void exportExpensesToCsv(String directoryPath, String fileName) throws DbConnectException, SQLException, IOException {


        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directoryPath + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            connection = DbConnector.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(EXPORT_EXPENSES_QUERY);
            csvWriter = new PrintWriter(new FileWriter(file));

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                csvWriter.print(metaData.getColumnName(i));
                if (i < columnCount) {
                    csvWriter.print(",");
                }
            }
            csvWriter.println();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    csvWriter.print(resultSet.getString(i));
                    if (i < columnCount) {
                        csvWriter.print(",");
                    }
                }
                csvWriter.println();
            }

            System.out.println("Data has been exported to " + file.getAbsolutePath());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (csvWriter != null) {
                csvWriter.close();
            }
        }
    }

}
