import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExpenseTrackerFrame extends JFrame {

    private ExpenseManager manager;

    private JTextField dateField;
    private JComboBox<String> categoryBox;
    private JTextField amountField;
    private JTextField noteField;

    private JTable table;
    private DefaultTableModel tableModel;

    private JLabel totalLabel;
    private JLabel categoryTotalLabel;

    // 🔹 NEW: filter fields
    private JTextField filterFromField;
    private JTextField filterToField;

    public ExpenseTrackerFrame(ExpenseManager manager) {
        this.manager = manager;
        initUI();
        loadExistingExpensesIntoTable();
    }

    private void initUI() {
        setTitle("Daily Expense Tracker (Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== Top Container (Form + Filter) =====
        JPanel topContainer = new JPanel(new BorderLayout());

        // ----- Form Panel -----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Date
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        dateField = new JTextField(10);
        dateField.setText(LocalDate.now().toString()); // default today
        formPanel.add(dateField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        String[] categories = {"Food", "Travel", "Bills", "Shopping", "Other"};
        categoryBox = new JComboBox<>(categories);
        formPanel.add(categoryBox, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        amountField = new JTextField(10);
        formPanel.add(amountField, gbc);

        // Note
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Note:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        noteField = new JTextField(30);
        formPanel.add(noteField, gbc);
        gbc.gridwidth = 1;

        // Buttons: Add, Show Total, Show Category Total, Clear, Delete, Export
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Expense");
        JButton showTotalButton = new JButton("Show Total");
        JButton showCategoryTotalButton = new JButton("Show Category Total");
        JButton clearButton = new JButton("Clear Fields");
        JButton deleteButton = new JButton("Delete Selected");
        JButton exportButton = new JButton("Export CSV");

        buttonPanel.add(addButton);
        buttonPanel.add(showTotalButton);
        buttonPanel.add(showCategoryTotalButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        formPanel.add(buttonPanel, gbc);
        gbc.gridwidth = 1;

        topContainer.add(formPanel, BorderLayout.NORTH);

        // ----- Filter Panel (Date Range) -----
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter by Date Range"));

        filterPanel.add(new JLabel("From (yyyy-MM-dd):"));
        filterFromField = new JTextField(10);
        filterPanel.add(filterFromField);

        filterPanel.add(new JLabel("To:"));
        filterToField = new JTextField(10);
        filterPanel.add(filterToField);

        JButton filterButton = new JButton("Filter");
        JButton showAllButton = new JButton("Show All");
        filterPanel.add(filterButton);
        filterPanel.add(showAllButton);

        topContainer.add(filterPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // ===== Center Panel: Table =====
        String[] columnNames = {"ID", "Date", "Category", "Amount", "Note"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table read-only
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ===== Bottom Panel: Totals =====
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        totalLabel = new JLabel("Total (all): 0.00");
        categoryTotalLabel = new JLabel("Category Total: 0.00");

        bottomPanel.add(totalLabel);
        bottomPanel.add(categoryTotalLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Button Actions =====
        addButton.addActionListener(e -> onAddExpense());
        showTotalButton.addActionListener(e -> onShowTotal());
        showCategoryTotalButton.addActionListener(e -> onShowCategoryTotal());
        clearButton.addActionListener(e -> onClearFields());
        deleteButton.addActionListener(e -> onDeleteSelected());
        exportButton.addActionListener(e -> onExportCsv());
        filterButton.addActionListener(e -> onFilterByDateRange());
        showAllButton.addActionListener(e -> onShowAll());
    }

    private void loadExistingExpensesIntoTable() {
        refreshTable(manager.getAllExpenses());
        onShowTotal();
    }

    private void refreshTable(List<Expense> list) {
        tableModel.setRowCount(0); // clear
        for (Expense expense : list) {
            tableModel.addRow(new Object[]{
                    expense.getId(),
                    expense.getDate(),
                    expense.getCategory(),
                    String.format("%.2f", expense.getAmount()),
                    expense.getNote()
            });
        }
    }

    private void onAddExpense() {
        try {
            String dateText = dateField.getText().trim();
            LocalDate date = LocalDate.parse(dateText);

            String category = (String) categoryBox.getSelectedItem();

            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showError("Please enter amount.");
                return;
            }
            double amount = Double.parseDouble(amountText);

            String note = noteField.getText().trim();

            Expense expense = manager.addExpense(date, category, amount, note);

            // Add to table (current view)
            tableModel.addRow(new Object[]{
                    expense.getId(),
                    expense.getDate(),
                    expense.getCategory(),
                    String.format("%.2f", expense.getAmount()),
                    expense.getNote()
            });

            // Reset fields
            amountField.setText("");
            noteField.setText("");
            onShowTotal();

        } catch (DateTimeParseException ex) {
            showError("Invalid date format. Use yyyy-MM-dd.");
        } catch (NumberFormatException ex) {
            showError("Invalid amount. Please enter a valid number.");
        } catch (Exception ex) {
            showError("Error adding expense: " + ex.getMessage());
        }
    }

    private void onShowTotal() {
        double total = manager.getTotalAmount();
        totalLabel.setText("Total (all): " + String.format("%.2f", total));
    }

    private void onShowCategoryTotal() {
        String category = (String) categoryBox.getSelectedItem();
        double total = manager.getTotalByCategory(category);
        categoryTotalLabel.setText("Category Total (" + category + "): " + String.format("%.2f", total));
    }

    private void onClearFields() {
        dateField.setText(LocalDate.now().toString());
        amountField.setText("");
        noteField.setText("");
    }

    private void onDeleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a row to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this expense?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Object idObj = tableModel.getValueAt(selectedRow, 0);
        int id = Integer.parseInt(idObj.toString());

        boolean removed = manager.removeExpenseById(id);
        if (removed) {
            // Reload current view (for simplicity, show all after delete)
            refreshTable(manager.getAllExpenses());
            onShowTotal();
            onShowCategoryTotal();
        } else {
            showError("Could not delete expense (not found in manager).");
        }
    }

    // 🔹 NEW: Export to CSV
    private void onExportCsv() {
        boolean ok = manager.exportToCsv("expenses_export.csv");
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Exported to expenses_export.csv in the app folder.",
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            showError("Failed to export CSV. Check console for details.");
        }
    }

    // 🔹 NEW: Filter by date range
    private void onFilterByDateRange() {
        String fromText = filterFromField.getText().trim();
        String toText = filterToField.getText().trim();

        if (fromText.isEmpty() || toText.isEmpty()) {
            showError("Please enter both From and To dates.");
            return;
        }

        try {
            LocalDate from = LocalDate.parse(fromText);
            LocalDate to = LocalDate.parse(toText);
            if (to.isBefore(from)) {
                showError("To date cannot be before From date.");
                return;
            }

            List<Expense> filtered = manager.getExpensesByDateRange(from, to);
            refreshTable(filtered);

            double sum = filtered.stream().mapToDouble(Expense::getAmount).sum();
            totalLabel.setText("Total (filtered): " + String.format("%.2f", sum));

        } catch (DateTimeParseException ex) {
            showError("Invalid date format in filter. Use yyyy-MM-dd.");
        }
    }

    // 🔹 NEW: Show all data again
    private void onShowAll() {
        refreshTable(manager.getAllExpenses());
        onShowTotal();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}