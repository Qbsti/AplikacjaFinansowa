package com.example.qbook.financeapp.Data.RoomDatabase.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.qbook.financeapp.Data.RoomDatabase.DateConverter;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;

import java.util.Date;
import java.util.List;

@Dao
public interface IncomeDao {
    @Query("SELECT * FROM incomes")
    List<Income> getAll();

    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM incomes WHERE income_date BETWEEN :dateYesterday AND :dateNow")
    LiveData<List<Income>> getGivenDateIncomes(Date dateYesterday, Date dateNow);

    @Query("SELECT * FROM incomes")
    LiveData<List<Income>> getAllAsLiveData();

    @Insert
    void insertAll(Income... income);

    @Delete
    void deleteIncome(Income income);
}

