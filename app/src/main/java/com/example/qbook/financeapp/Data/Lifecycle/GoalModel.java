package com.example.qbook.financeapp.Data.Lifecycle;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.qbook.financeapp.Data.Repository.GoalRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;

import java.util.List;

public class GoalModel extends ViewModel {
    private GoalRepository goalRepository;

    public GoalModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public LiveData<List<Goal>> getAllAsLiveData() {
        return goalRepository.getAllGoals();
    }

    public LiveData<Goal> getGoalByName(String name) {
        return goalRepository.getGoalByName(name);
    }

    public LiveData<Goal> getGoalById(Integer id) {
        return goalRepository.getGoalById(id);
    }

    public void updateGoal(Goal goal) {
        UpdateGoalTask task = new UpdateGoalTask();
        task.execute(goal);
    }

    private class UpdateGoalTask extends AsyncTask<Goal, Void, Void> {
        @Override
        protected Void doInBackground(Goal... goals) {
            goalRepository.updateGoal(goals[0]);
            return null;
        }
    }

    public void deleteGoal(Goal goal) {
        DeleteGoalTask task = new DeleteGoalTask();
        task.execute(goal);
    }

    private class DeleteGoalTask extends AsyncTask<Goal, Void, Void> {
        @Override
        protected Void doInBackground(Goal... goals) {
            goalRepository.deleteGoal(goals[0]);
            return null;
        }
    }

    public void addNewGoal(Goal goal){
        new AddGoalTask().execute(goal);
    }

    private class AddGoalTask extends AsyncTask<Goal, Void, Void> {

        @Override
        protected Void doInBackground(Goal... goals) {
            goalRepository.saveGoals(goals);
            return null;
        }
    }

}
