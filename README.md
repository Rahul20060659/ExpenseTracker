# Expense Tracker

A simple Java-based Expense Tracking System built using **Java Swing** and **JDBC** (SQLite).  
This project allows users to track their expenses, add, view, and manage them through a desktop GUI.

---

## Features

- **User Registration & Login**  
  Secure login system to manage individual user accounts.

- **Add Expenses**  
  Track expenses by category, amount, date, and description.

- **View Expenses**  
  View all your expenses in a table format.

- **Edit/Delete Expenses**  
  Update or remove expenses if needed.

- **Dashboard**  
  Summarizes total expenses and displays them in an organized way.

---

## Project Structure

ExpenseTracker/
├── src/
│ └── com/expensetracker/
│ ├── Main.java
│ ├── model/ # User and Expense classes
│ ├── dao/ # Database access objects
│ ├── util/ # Database and password utilities
│ └── view/ # GUI frames (Swing)
├── lib/
│ └── sqlite-jdbc-3.42.0.0.jar
└── expense_tracker.db # SQLite database (local only, not pushed)





---

## Installation

1. Clone the repository:

```bash
git clone https://github.com/Rahul20060659/ExpenseTracker.git





Open the project in IntelliJ IDEA (or any Java IDE).

Add sqlite-jdbc-3.42.0.0.jar to the project libraries.

Run Main.java to launch the application.
