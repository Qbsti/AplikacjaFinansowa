package com.example.qbook.financeapp.Data.RoomDatabase.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;

import java.util.List;

@Dao
public interface GoalDao {
    @Query("SELECT * FROM goals")
    List<Goal> getAll();

    @Query("SELECT * FROM goals")
    LiveData<List<Goal>> getAllAsLiveData();

    @Query("SELECT * FROM goals WHERE goal_name LIKE :name LIMIT 1")
    LiveData<Goal> getGoalByName(String name);

    @Query("SELECT * FROM goals WHERE goal_id LIKE :id LIMIT 1")
    LiveData<Goal> getGoalById(Integer id);

    @Delete
    void deleteGoal(Goal goal);

    @Insert
    void insertAll(Goal... goal);

    @Update
    void updateGoal(Goal goal);
}
