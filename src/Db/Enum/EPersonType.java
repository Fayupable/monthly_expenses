package Db.Enum;

public enum EPersonType {
    MEMBER("MEMBER"),
    MODERATOR("MODERATOR"),
    ADMIN("ADMIN");

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