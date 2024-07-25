package Db.Tables;

import Db.Enum.EPaymentMethods;

public class PaymentMethods {
    private int id;
    private EPaymentMethods paymentMethod;

    public PaymentMethods() {
    }

    public PaymentMethods(EPaymentMethods paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EPaymentMethods getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(EPaymentMethods paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "PaymentMethods{" +
                "id=" + id +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
