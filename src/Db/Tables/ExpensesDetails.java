package Db.Tables;

import java.math.BigDecimal;

public class ExpensesDetails {
    private int id;
    private int expense_id;
    private String item;
    private BigDecimal cost;

    public ExpensesDetails() {
    }

    public ExpensesDetails(int expense_id, String item, BigDecimal cost) {
        this.expense_id = expense_id;
        this.item = item;
        this.cost = cost;
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

    @Override
    public String toString() {
        return "ExpensesDetails{" + "id=" + id + ", expense_id=" + expense_id + ", item='" + item + '\'' + ", cost=" + cost + '}';
    }
}
