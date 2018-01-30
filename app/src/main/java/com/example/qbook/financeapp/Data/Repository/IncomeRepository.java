package com.example.qbook.financeapp.Data.Repository;

import android.arch.lifecycle.LiveData;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.IncomeDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;

import java.util.List;

import javax.inject.Inject;

public class IncomeRepository {
    private final IncomeDao incomeDao;

    @Inject
    public IncomeRepository(IncomeDao incomeDao) {
        this.incomeDao = incomeDao;
    }

    public LiveData<List<Income>> getAllIncomesAsLiveData() {
        return incomeDao.getAllAsLiveData();
    }

    public void saveIncomes(Income... incomes) {
        incomeDao.insertAll(incomes);
    }

    public void deleteIncome(Income income) {
        incomeDao.deleteIncome(income);
    }
}
