package com.example.qbook.financeapp.Data.Lifecycle;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.qbook.financeapp.Data.Repository.IncomeCategoryRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;

import java.util.List;

public class IncomeCategoryModel extends ViewModel {
   private IncomeCategoryRepository incomeCategoryRepository;

    public IncomeCategoryModel(IncomeCategoryRepository incomeCategoryRepository) {
        this.incomeCategoryRepository = incomeCategoryRepository;
    }

    public LiveData<List<IncomeCategory>> getAllAsLiveData() {
        return incomeCategoryRepository.getAllAsLiveData();
    }

    private void deleteInccomeCategory(IncomeCategory incomeCategory) {
        DeleteIncomeCategoryTask task = new DeleteIncomeCategoryTask();
        task.execute(incomeCategory);
    }

    private class DeleteIncomeCategoryTask extends AsyncTask<IncomeCategory, Void, Void> {
        @Override
        protected Void doInBackground(IncomeCategory... incomeCategories) {
            incomeCategoryRepository.deleteIncomeCategory(incomeCategories[0]);
            return null;
        }
    }

    public void addNewIncomeCategory(IncomeCategory incomeCategory){
        new AddNewIncomeCategoryTask().execute(incomeCategory);
    }

    private class AddNewIncomeCategoryTask extends AsyncTask<IncomeCategory, Void, Void> {

        @Override
        protected Void doInBackground(IncomeCategory... incomeCategories) {
            incomeCategoryRepository.saveIncomeCategories(incomeCategories);
            return null;
        }
    }

}