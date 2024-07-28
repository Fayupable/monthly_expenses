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
    public static EPaymentMethods fromDisplayName(String displayName) {
        for (EPaymentMethods method : EPaymentMethods.values()) {
            if (method.getDisplayName().equals(displayName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name " + displayName);
    }
}