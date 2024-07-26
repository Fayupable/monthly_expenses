import Db.DbFunction;
import Db.Enum.ECategoryType;
import Db.Enum.EPaymentMethods;
import Db.Enum.EPersonType;
import Db.Exception.DbConnectException;
import Db.Exception.DbInsertException;
import Db.Tables.Categories;
import Db.Tables.PaymentMethods;
import Db.Tables.Persons;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
//        DbFunction dbFunction = new DbFunction();
//        //nakit, kredi kartı, banka kartı, yemek çeki, veresiye
//        PaymentMethods paymentMethods = new PaymentMethods();
//        paymentMethods.setPaymentMethod(EPaymentMethods.CREDIT);
//
//        try {
//            dbFunction.insertPaymentMethod(paymentMethods);
//        } catch (DbConnectException e) {
//            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        DbFunction dbFunction = new DbFunction();
//
//        for (ECategoryType categoryType : ECategoryType.values()) {
//            Categories category = new Categories();
//            category.setName(categoryType.name());
//
//            try {
//                dbFunction.insertCategory(category);
//            } catch (DbConnectException | SQLException e) {
//                e.printStackTrace();
//            } catch (DbInsertException e) {
//                throw new RuntimeException(e);
//            }
//        }





    }
}