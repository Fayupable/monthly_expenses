package Db.Tables;

import java.math.BigDecimal;

public class ExpensesDetails {
    private int id;
    private int expense_id;
    private String item;
    private BigDecimal cost;
    private BigDecimal amount;

    public ExpensesDetails() {
    }

    public ExpensesDetails(int expense_id, String item, BigDecimal cost, BigDecimal amount) {
        this.expense_id = expense_id;
        this.item = item;
        this.cost = cost;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    @Override
    public String toString() {
        return "ExpensesDetails{" + "id=" + id + ", expense_id=" + expense_id + ", item='" + item + '\'' + ", cost=" + cost + '}';
    }
}
