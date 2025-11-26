📊 Daily Expense Tracker – Java Swing Desktop Application

A desktop-based expense tracking app built using Java Swing. It allows users to register, log in, and manage their daily expenses via an interactive GUI. Each user gets a separate expense file, and can view, filter, and delete expenses.

🚀 Features

🔐 User Login & Registration

🧾 Add daily expenses

📅 Filter by date range

📊 View total & category-wise spending

🗑 Delete expenses

💾 Data stored in user-specific text files
expenses_<username>.txt

📤 Export to CSV (planned)

🛠 Technologies Used
Technology	Purpose
Java (Core + OOP)	Backend logic
Java Swing / AWT	GUI
Java I/O	File storage
LocalDate API	Date handling
VS Code	IDE
📦 Project Structure
DailyExpenseTrackerSwing/
├── Main.java
├── LoginFrame.java
├── RegistrationFrame.java
├── ExpenseTrackerFrame.java
├── ExpenseManager.java
├── Expense.java
├── users.txt
└── expenses_<username>.txt

▶ How to Run (VS Code)
1. Open project in VS Code
2. Run Main.java
3. Register a user → Login → Start tracking expenses

🔮 Future Enhancements

Move to SQLite/MySQL using JDBC

Add password hashing (BCrypt)

Add graphs & charts

Export to PDF & Excel

Edit expenses feature