package Db.Tables;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Expenses implements Comparable<Expenses> {
    private int id;
    private int person_id;
    private BigDecimal amount;
    private BigDecimal cost;
    private String description;
    private int category_id;
    private int payment_method_id;
    private Date date;


    public Expenses() {
    }

    public Expenses(int person_id, BigDecimal amount, BigDecimal cost, String description, int category_id, int payment_method_id, Date date) {
        this.person_id = person_id;
        this.amount = amount;
        this.cost = cost;
        this.description = description;
        this.category_id = category_id;
        this.payment_method_id = payment_method_id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(int payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;

    }

    @Override
    public String toString() {
        return "Expenses{" +
                "id=" + id +
                ", person_id=" + person_id +
                ", amount=" + amount +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                ", category_id=" + category_id +
                ", payment_method_id=" + payment_method_id +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(@NotNull Expenses o) {
        return 0;
    }
}
