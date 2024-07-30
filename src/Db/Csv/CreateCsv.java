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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class CreateCsv {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PrintWriter csvWriter;


    private static final String EXPORT_EXPENSES_QUERY = "SELECT * FROM expenses WHERE person_id = ?";


    public String getUserDownloadsFolder() {
        String userHome = System.getProperty("user.home");
        Path downloadsPath = Paths.get(userHome, "Downloads");
        return downloadsPath.toString();
    }

    public void exportExpenses(String fileType, String fileName, int personId) {
        String downloadsDirectory = getUserDownloadsFolder();
        switch (fileType.toLowerCase()) {
            case "csv":
                exportExpensesToCsv(downloadsDirectory, fileName, personId);
                break;
            case "excel":
                exportExpensesToExcel(downloadsDirectory, fileName, personId);
                break;
            case "xml":
                exportExpensesToXml(downloadsDirectory, fileName, personId);
                break;
            default:
                throw new IllegalArgumentException("Invalid file type: " + fileType);
        }
    }

    public void exportExpensesToCsv(String downloadsDirectory, String fileName, int personId) {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXPORT_EXPENSES_QUERY)) {

            preparedStatement.setInt(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();

            String filePath = downloadsDirectory + File.separator + fileName;
            try (PrintWriter csvWriter = new PrintWriter(new FileWriter(filePath))) {
                csvWriter.println("ID,Amount,Category,Payment Method,Cost,Description,Date");

                while (resultSet.next()) {
                    csvWriter.println(resultSet.getInt("id") + "," +
                            resultSet.getDouble("amount") + "," +
                            resultSet.getString("category") + "," +
                            resultSet.getString("payment_method") + "," +
                            resultSet.getBigDecimal("cost").doubleValue() + "," +
                            resultSet.getString("description") + "," +
                            resultSet.getTimestamp("date").toString());
                }

                System.out.println("CSV data has been exported to " + filePath);
            }
        } catch (SQLException | IOException | DbConnectException e) {
            e.printStackTrace();
        }
    }

    public void exportExpensesToExcel(String downloadsDirectory, String fileName, int personId) {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXPORT_EXPENSES_QUERY)) {

            preparedStatement.setInt(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Expenses");

            int rowNumber = 0;
            Row row = sheet.createRow(rowNumber);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Amount");
            row.createCell(2).setCellValue("category_id");
            row.createCell(3).setCellValue("Payment_Method_id");
            row.createCell(4).setCellValue("Cost");
            row.createCell(5).setCellValue("Description");
            row.createCell(6).setCellValue("Date");

            while (resultSet.next()) {
                rowNumber++;
                row = sheet.createRow(rowNumber);
                row.createCell(0).setCellValue(resultSet.getInt("id"));
                row.createCell(1).setCellValue(resultSet.getDouble("amount"));
                row.createCell(2).setCellValue(resultSet.getString("category_id"));
                row.createCell(3).setCellValue(resultSet.getString("payment_method_id"));
                row.createCell(4).setCellValue(resultSet.getBigDecimal("cost").doubleValue());
                row.createCell(5).setCellValue(resultSet.getString("description"));
                row.createCell(6).setCellValue(resultSet.getTimestamp("date").toString());

            }

            String filePath = downloadsDirectory + File.separator + fileName;
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Klasörleri oluşturur

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                workbook.write(fileOutputStream);
                System.out.println("Excel data has been exported to " + filePath);
            }
            workbook.close();
        } catch (SQLException | IOException | DbConnectException e) {
            e.printStackTrace();
        }
    }

    public void exportExpensesToXml(String downloadsDirectory, String fileName, int personId) {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXPORT_EXPENSES_QUERY)) {

            preparedStatement.setInt(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();

            String filePath = downloadsDirectory + File.separator + fileName;
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Klasörleri oluşturur

            try (PrintWriter xmlWriter = new PrintWriter(new FileWriter(filePath))) {
                xmlWriter.println("<Expenses>");

                while (resultSet.next()) {
                    xmlWriter.println("  <Expense>");
                    xmlWriter.println("    <ID>" + resultSet.getInt("id") + "</ID>");
                    xmlWriter.println("    <Amount>" + resultSet.getDouble("amount") + "</Amount>");
                    xmlWriter.println("    <Category>" + resultSet.getString("category_id") + "</Category>");
                    xmlWriter.println("    <PaymentMethod>" + resultSet.getString("payment_method_id") + "</PaymentMethod>");
                    xmlWriter.println("    <Cost>" + resultSet.getBigDecimal("cost").doubleValue() + "</Cost>");
                    xmlWriter.println("    <Description>" + resultSet.getString("description") + "</Description>");
                    xmlWriter.println("    <Date>" + resultSet.getTimestamp("date").toString() + "</Date>");
                    xmlWriter.println("  </Expense>");
                }

                xmlWriter.println("</Expenses>");
                System.out.println("XML data has been exported to " + filePath);
            }
        } catch (SQLException | IOException | DbConnectException e) {
            e.printStackTrace();
        }
    }
}
