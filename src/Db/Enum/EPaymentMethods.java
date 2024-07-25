package Db.Enum;

public enum EPaymentMethods {
    CASH("Nakit"),
    CREDIT_CARD("Kredi Kartı"),
    DEBIT_CARD("Banka Kartı"),
    CREDIT("Veresiye"),
    MEAL_VOUCHER("Yemek Çeki");

    private final String displayName;

    EPaymentMethods(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}