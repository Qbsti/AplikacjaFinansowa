package com.example.qbook.financeapp.Data.Repository;

import android.arch.lifecycle.LiveData;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseCategoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;

import java.util.List;

import javax.inject.Inject;

public class ExpenseCategoryRepository {
    private final ExpenseCategoryDao expenseCategoryDao;

    @Inject
    public ExpenseCategoryRepository(ExpenseCategoryDao expenseCategoryDao) {
        this.expenseCategoryDao = expenseCategoryDao;
    }

    public LiveData<List<ExpenseCategory>> getAllAsLiveData() {
        return expenseCategoryDao.getAllAsLiveData();
    }

    public void saveExpenseCategories(ExpenseCategory... expenseCategories) {
        expenseCategoryDao.insertAll(expenseCategories);
    }


    public void deleteExpenseCategory(ExpenseCategory expenseCategory) {
        expenseCategoryDao.deleteExpenseCategory(expenseCategory);
    }
}
