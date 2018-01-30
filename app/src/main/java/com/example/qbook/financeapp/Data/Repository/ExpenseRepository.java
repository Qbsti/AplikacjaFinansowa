package com.example.qbook.financeapp.Data.Repository;


import android.arch.lifecycle.LiveData;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;

import java.util.List;

import javax.inject.Inject;

public class ExpenseRepository {
    private final ExpenseDao expenseDao;

    @Inject
    public ExpenseRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return expenseDao.getAllAsLiveData();
    }

    public void saveExpenses(Expense... expenses) {
        expenseDao.insertAll(expenses);
    }

    public void deleteExpense(Expense expense) {
        expenseDao.deleteExpense(expense);
    }
}
