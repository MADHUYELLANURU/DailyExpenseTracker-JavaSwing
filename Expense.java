import java.time.LocalDate;
public class Expense {
    private int id;
    private LocalDate date;
    private String category;
    private double amount;
    private String note;

    public Expense(int id, LocalDate date, String category, double amount, String note) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }
}