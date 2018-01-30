package com.example.qbook.financeapp.Data.Lifecycle;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.qbook.financeapp.Data.Repository.ExpenseCategoryRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;

import java.util.List;

public class ExpenseCategoryModel extends ViewModel {
    private ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseCategoryModel(ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    public LiveData<List<ExpenseCategory>> getAllAsLiveData() {
        return expenseCategoryRepository.getAllAsLiveData();
    }

    private void deleteExpenseCategory(ExpenseCategory expenseCategory) {
        DeleteExpenseCategoryTask task = new DeleteExpenseCategoryTask();
        task.execute(expenseCategory);
    }

    private class DeleteExpenseCategoryTask extends AsyncTask<ExpenseCategory, Void, Void> {
        @Override
        protected Void doInBackground(ExpenseCategory... expenseCategories) {
            expenseCategoryRepository.deleteExpenseCategory(expenseCategories[0]);
            return null;
        }
    }
    public void addNewExpenseCategory(ExpenseCategory expenseCategory){
        new AddNewExpenseCategoryTask().execute(expenseCategory);
    }

    private class AddNewExpenseCategoryTask extends AsyncTask<ExpenseCategory, Void, Void> {

        @Override
        protected Void doInBackground(ExpenseCategory... expenseCategories) {
            expenseCategoryRepository.saveExpenseCategories(expenseCategories);
            return null;
        }
    }

}
