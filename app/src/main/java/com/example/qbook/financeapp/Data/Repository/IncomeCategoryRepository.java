package com.example.qbook.financeapp.Data.Repository;

import android.arch.lifecycle.LiveData;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.IncomeCategoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;

import java.util.List;

import javax.inject.Inject;

public class IncomeCategoryRepository {

    private final IncomeCategoryDao incomeCategoryDao;

    @Inject
    public IncomeCategoryRepository(IncomeCategoryDao incomeCategoryDao) {
        this.incomeCategoryDao = incomeCategoryDao;
    }

    public LiveData<List<IncomeCategory>> getAllAsLiveData() {
        return incomeCategoryDao.getAllAsLiveData();
    }

    public void saveIncomeCategories(IncomeCategory... incomeCategories) {
        incomeCategoryDao.insertAll(incomeCategories);
    }

    public void deleteIncomeCategory(IncomeCategory incomeCategory) {
        incomeCategoryDao.deleteIncomeCategory(incomeCategory);
    }

}
