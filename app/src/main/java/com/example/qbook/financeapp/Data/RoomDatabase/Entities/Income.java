package com.example.qbook.financeapp.Data.RoomDatabase.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.qbook.financeapp.Data.RecurrenceType;
import com.example.qbook.financeapp.Data.RoomDatabase.DateConverter;
import com.example.qbook.financeapp.Data.RoomDatabase.EnumConverter;

import java.util.Date;
@Entity(tableName = "incomes",
        foreignKeys = @ForeignKey(entity = IncomeCategory.class,
                parentColumns = "income_category_name",
                childColumns = "income_category"),
        indices = @Index(value="income_category"))
public class Income implements Comparable<Income>{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "income_id")
    private int id;
    @ColumnInfo(name = "income_category")
    private  String categoryName;
    @ColumnInfo(name = "income_name")
    private  String name;
    @ColumnInfo(name = "income_value")
    private  float value;
    @ColumnInfo(name = "income_date")
    @TypeConverters(DateConverter.class)
    private  Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Income{" +
                "categoryName='" + categoryName + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(@NonNull Income income) {
        if (income.getDate().before(this.getDate())) {
            return 1;
        } else if (income.getDate().after(this.getDate())) {
            return -1;
        } else {
            return 0;
        }    }
}
