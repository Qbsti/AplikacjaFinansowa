package com.example.qbook.financeapp.Data.RoomDatabase.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.qbook.financeapp.Data.RoomDatabase.DateConverter;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

@Dao
public interface ExpenseCategoryDao {
    @Query("SELECT * FROM expense_categories")
    List<ExpenseCategory> getAll();

    @Query("SELECT * FROM expense_categories")
    LiveData<List<ExpenseCategory>> getAllAsLiveData();

    @Query("SELECT * FROM expense_categories WHERE expense_category_name LIKE :name LIMIT 1")
    ExpenseCategory getExpenseCategoryByName(String name);

    @Insert
    void insertAll(ExpenseCategory... expenseCategory);

    @Delete
    void deleteExpenseCategory(ExpenseCategory expenseCategory);
}
