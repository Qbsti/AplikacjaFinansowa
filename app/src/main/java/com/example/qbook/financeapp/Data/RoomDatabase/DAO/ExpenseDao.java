package com.example.qbook.financeapp.Data.RoomDatabase.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.qbook.financeapp.Data.RoomDatabase.DateConverter;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;

import java.util.Date;
import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    List<Expense> getAll();

    @Query("SELECT * FROM expenses")
    LiveData<List<Expense>> getAllAsLiveData();

    @Insert
    void insertAll(Expense... expense);

    @Delete
    void deleteExpense(Expense expense);

    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM expenses WHERE expense_date BETWEEN :dateYesterday AND :dateNow")
    LiveData<List<Expense>> getGivenDateExpenses(Date dateYesterday, Date dateNow);

    @Query("SELECT * FROM expenses WHERE expense_category LIKE :expenseCategoryName")
    LiveData<List<Expense>> getAllExpensesForCategoryAsLiveData(String expenseCategoryName);

    @Query("SELECT * FROM expenses WHERE expense_category LIKE :expenseCategoryName")
    List<Expense> getAllExpensesForCategory(String expenseCategoryName);

    }
