package Db.Csv;


import Db.DbConnector;
import Db.Exception.DbConnectException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.*;
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

    public void exportExpensesToExcel(@NotNull String directoryPath, @NotNull String fileName) throws DbConnectException, SQLException, IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

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

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Başlık satırını yazma
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(metaData.getColumnName(i));
            }

            // Veri satırlarını yazma
            int rowIndex = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(resultSet.getString(i));
                }
            }

            // Excel dosyasını kaydet
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
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
            workbook.close();
        }
    }
}
