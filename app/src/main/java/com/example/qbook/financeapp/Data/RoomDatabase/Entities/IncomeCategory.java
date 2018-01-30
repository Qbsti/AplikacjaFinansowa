package com.example.qbook.financeapp.Data.RoomDatabase.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "income_categories")
public class IncomeCategory {
    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "income_category_name")
    private  String name;

    public IncomeCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IncomeCategory{" +
                "name='" + name + '\'' +
                '}';
    }
}
