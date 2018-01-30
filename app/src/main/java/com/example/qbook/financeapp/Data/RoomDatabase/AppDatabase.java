package com.example.qbook.financeapp.Data.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseCategoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.GoalDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.IncomeCategoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.IncomeDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.WalletBalanceHistoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.WalletDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Wallet;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.WalletBalanceHistory;


@Database(entities =
        {Expense.class, Income.class, ExpenseCategory.class, IncomeCategory.class, Wallet.class, WalletBalanceHistory.class, Goal.class},
        version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();

    public abstract ExpenseCategoryDao expenseCategoryDao();

    public abstract IncomeDao incomeDao();

    public abstract IncomeCategoryDao incomeCategoryDao();

    public abstract WalletDao walletDao();

    public abstract WalletBalanceHistoryDao walletBalanceHistoryDao();

    public abstract GoalDao goalDao();


}