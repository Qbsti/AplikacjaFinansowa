package com.example.qbook.financeapp.Data.Lifecycle;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.qbook.financeapp.Data.Repository.ExpenseRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;

import java.util.List;

public class ExpenseModel extends ViewModel {
    private ExpenseRepository expenseRepository;

    public ExpenseModel(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public LiveData<List<Expense>> getAllAsLiveData() {
        return expenseRepository.getAllExpenses();
    }

    public void deleteExpense(Expense expense) {
        DeleteExpenseTask task = new DeleteExpenseTask();
        task.execute(expense);
    }

    private class DeleteExpenseTask extends AsyncTask<Expense, Void, Void> {
        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseRepository.deleteExpense(expenses[0]);
            return null;
        }
    }

    public void addNewExpense(Expense expense){
        new AddNewExpenseTask().execute(expense);
    }

    private class AddNewExpenseTask extends AsyncTask<Expense, Void, Void> {

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseRepository.saveExpenses(expenses);
            return null;
        }
    }

}
