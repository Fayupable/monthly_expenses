package Db.Enum;

public enum EPersonType {
    MEMBER("Üye"),
    MODERATOR("Yetkili"),
    ADMIN("Admin");

    private final String displayName;

    EPersonType(String displayName) {
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