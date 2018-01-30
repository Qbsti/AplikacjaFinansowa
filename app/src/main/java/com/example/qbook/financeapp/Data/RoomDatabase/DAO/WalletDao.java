package com.example.qbook.financeapp.Data.RoomDatabase.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Wallet;

import java.util.List;

@Dao
public interface WalletDao {
    @Query("SELECT * FROM wallets")
    List<Wallet> getAll();

    @Query("SELECT * FROM wallets")
    LiveData<List<Wallet>> getAllAsLiveData();

}
