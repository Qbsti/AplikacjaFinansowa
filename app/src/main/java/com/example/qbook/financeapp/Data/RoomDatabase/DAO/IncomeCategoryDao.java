package com.example.qbook.financeapp.Data.RoomDatabase.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;

import java.util.List;

@Dao
public interface IncomeCategoryDao {
    @Query("SELECT * FROM income_categories")
    List<IncomeCategory> getAll();

    @Query("SELECT * FROM income_categories")
    LiveData<List<IncomeCategory>> getAllAsLiveData();

    @Insert
    void insertAll(IncomeCategory... incomeCategories);

    @Delete
    void deleteIncomeCategory(IncomeCategory incomeCategory);
}
