package com.example.qbook.financeapp.Data.Repository;

import android.arch.lifecycle.LiveData;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.GoalDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;

import java.util.List;

import javax.inject.Inject;

public class GoalRepository {
    private final GoalDao goalDao;

    @Inject
    public GoalRepository(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public LiveData<List<Goal>> getAllGoals() {
        return goalDao.getAllAsLiveData();
    }

    public LiveData<Goal> getGoalByName(String name) {
        return goalDao.getGoalByName(name);
    }

    public LiveData<Goal> getGoalById(Integer id) {
        return goalDao.getGoalById(id);
    }

    public void deleteGoal(Goal goal) {
        goalDao.deleteGoal(goal);
    }

    public void saveGoals(Goal... goals) {
        goalDao.insertAll(goals);
    }

    public void updateGoal(Goal goal) {goalDao.updateGoal(goal);}
}
