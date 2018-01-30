package com.example.qbook.financeapp.Data.RoomDatabase.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "wallets")

public class Wallet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    private int id;
    @ColumnInfo(name = "wallet_currency")
    private  String currency;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "currency='" + currency + '\'' +
                '}';
    }
}
