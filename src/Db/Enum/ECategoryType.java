package Db.Enum;

public enum ECategoryType {
    YEMEK(1),
    ICECEK(2),
    TATLI(3),
    EGLENCE(4),
    KITAP(5),
    EV(6),
    OFIS(7),
    GIYIM(8),
    ARABA(9),
    BENZIN(10),
    AKSESUAR(11),
    TEMIZLIK(12),
    SAGLIK(13),
    SPOR(14),
    KIRTASIYE(15),
    HEDIYE(16),
    BILGISAYAR(17),
    TELEFON(18),
    INTERNET(19),
    ELEKTRIK(20),
    SU(21),
    DOGALGAZ(22),
    KIRA(23),

    DIGER(99);

    private final int id;

    ECategoryType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ECategoryType fromId(int id) {
        for (ECategoryType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ID: " + id);
    }
}