package Db.Tables;

import Db.Enum.EPersonType;

import java.sql.Timestamp;

public class Persons {
    private int id;
    private String name;
    private String e_mail;
    private String password;
    private EPersonType person_type;
    private Timestamp created_at;

    public Persons() {
    }

    public Persons(String name, String e_mail, String password, EPersonType person_type, Timestamp created_at) {
        this.name = name;
        this.e_mail = e_mail;
        this.password = password;
        this.person_type = person_type;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EPersonType getPerson_type() {
        return person_type;
    }

    public void setPerson_type(EPersonType person_type) {
        this.person_type = person_type;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Persons{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", password='" + password + '\'' +
                ", person_type=" + person_type +
                ", created_at=" + created_at +
                '}';
    }
}
