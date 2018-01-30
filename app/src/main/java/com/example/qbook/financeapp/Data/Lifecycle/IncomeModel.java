package com.example.qbook.financeapp.Data.Lifecycle;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.qbook.financeapp.Data.Repository.IncomeRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;

import java.util.List;

public class IncomeModel extends ViewModel {
    private IncomeRepository incomeRepository;

    public IncomeModel(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }


    public LiveData<List<Income>> getAllAsLiveData() {
        return incomeRepository.getAllIncomesAsLiveData();
    }

    public void deleteIncome(Income income) {
        DeleteIncomeTask task = new DeleteIncomeTask();
        task.execute(income);
    }

    private class DeleteIncomeTask extends AsyncTask<Income, Void, Void> {
        @Override
        protected Void doInBackground(Income... incomes) {
            incomeRepository.deleteIncome(incomes[0]);
            return null;
        }
    }

    public void addNewIncome(Income income){
        new AddNewIncomeTask().execute(income);
    }

    private class AddNewIncomeTask extends AsyncTask<Income, Void, Void> {

        @Override
        protected Void doInBackground(Income... incomes) {
            incomeRepository.saveIncomes(incomes);
            return null;
        }
    }
}
