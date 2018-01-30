package com.example.qbook.financeapp.Data.RoomDatabase.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.qbook.financeapp.Data.RoomDatabase.DateConverter;

import java.util.Date;

@Entity(tableName = "wallet_balance_history", foreignKeys = @ForeignKey(
        entity = Wallet.class,
        parentColumns = "wallet_id",
        childColumns = "wallet_id"),
        indices = @Index(value = "wallet_id"))
public class WalletBalanceHistory implements Comparable<WalletBalanceHistory> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_balance_history_id")
    private int id;
    @ColumnInfo(name = "wallet_id")
    private String walletId;
    @ColumnInfo(name = "wallet_balance")
    private Float walletBalance;
    @ColumnInfo(name = "wallet_balance_date")
    @TypeConverters(DateConverter.class)
    private Date walletBalanceDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Float getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Float walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Date getWalletBalanceDate() {
        return walletBalanceDate;
    }

    public void setWalletBalanceDate(Date walletBalanceDate) {
        this.walletBalanceDate = walletBalanceDate;
    }


    @Override
    public int compareTo(@NonNull WalletBalanceHistory walletBalanceHistory) {
        if (walletBalanceHistory.getWalletBalanceDate().before(this.getWalletBalanceDate())) {
            return 1;
        } else if (walletBalanceHistory.getWalletBalanceDate().after(this.getWalletBalanceDate())) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "WalletBalanceHistory{" +
                "walletBalance=" + walletBalance +
                ", walletBalanceDate=" + walletBalanceDate +
                '}';
    }
}
