import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseManager {

    private List<Expense> expenses = new ArrayList<>();
    private int nextId = 1;
    private String DATA_FILE; // Now dynamic per user

    // 🚀 NEW Constructor - pass username
    public ExpenseManager(String username) {
        this.DATA_FILE = "expenses_" + username + ".txt";  // Example: expenses_admin.txt
    }

    public Expense addExpense(LocalDate date, String category, double amount, String note) {
        Expense expense = new Expense(nextId++, date, category, amount, note);
        expenses.add(expense);
        saveToFile();
        return expense;
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    public double getTotalAmount() {
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getTotalByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public List<Expense> getExpensesByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public boolean removeExpenseById(int id) {
        boolean removed = expenses.removeIf(e -> e.getId() == id);
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    public void loadFromFile() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) return;

            List<Expense> loaded = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) continue;

                int id = Integer.parseInt(parts[0].trim());
                LocalDate date = LocalDate.parse(parts[1].trim());
                String category = parts[2].trim();
                double amount = Double.parseDouble(parts[3].trim());
                String note = parts[4];

                loaded.add(new Expense(id, date, category, amount, note));
                maxId = Math.max(maxId, id);
            }
            reader.close();

            this.expenses = loaded;
            this.nextId = maxId + 1;
        } catch (Exception e) {
            System.err.println("Error loading expenses: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));
            for (Expense e : expenses) {
                String noteSafe = e.getNote() == null ? "" : e.getNote().replace("|", "/");
                writer.write(e.getId() + "|" +
                             e.getDate() + "|" +
                             e.getCategory() + "|" +
                             e.getAmount() + "|" +
                             noteSafe);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }

    List<Expense> getExpensesByDateRange(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    boolean exportToCsv(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
