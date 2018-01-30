package com.example.qbook.financeapp.Data.RoomDatabase.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.example.qbook.financeapp.Data.RoomDatabase.DateConverter;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.WalletBalanceHistory;

import java.util.Date;
import java.util.List;

@Dao
public interface WalletBalanceHistoryDao {
    @Query("SELECT * FROM wallet_balance_history")
    List<WalletBalanceHistory> getAll();

    @Query("SELECT * FROM wallet_balance_history")
    LiveData<List<WalletBalanceHistory>> getAllAsLiveData();

    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM wallet_balance_history WHERE wallet_balance_date BETWEEN :dateYesterday AND :dateNow LIMIT 1")
    WalletBalanceHistory getGivenDateBalance(Date dateYesterday, Date dateNow);

    @Insert
    void insertAll(WalletBalanceHistory... walletBalanceHistories);

    @Insert
    void insertAllFromList(List<WalletBalanceHistory> walletBalanceHistories);

    @Update
    void updateBalanceEntries(WalletBalanceHistory... walletBalanceHistory);

    @Delete
    void deleteWalletBalanceHistory(WalletBalanceHistory walletBalanceHistory);

    @Query("DELETE FROM wallet_balance_history")
    void eraseAll();

    @Delete
    void deleteAllGivenHistory(WalletBalanceHistory... walletBalanceHistories);
}
