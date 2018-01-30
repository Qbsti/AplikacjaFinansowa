package com.example.qbook.financeapp.Data.RoomDatabase.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    private int id;
    @ColumnInfo(name = "goal_name")
    private String name;
    @ColumnInfo(name = "goal_value")
    private float value;
    @ColumnInfo(name = "goal_completion")
    private boolean isDone;
    @ColumnInfo(name = "money_saved_for_goal")
    private float moneySavedForGoal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public float getMoneySavedForGoal() {
        return moneySavedForGoal;
    }

    public void setMoneySavedForGoal(float moneySavedForGoal) {
        this.moneySavedForGoal = moneySavedForGoal;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", isDone=" + isDone +
                ", moneySavedForGoal=" + moneySavedForGoal +
                '}';
    }
}
