package com.example.qbook.financeapp.Data.Repository;

import android.arch.lifecycle.LiveData;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.WalletDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Wallet;

import java.util.List;

import javax.inject.Inject;

public class WalletRepository {
    private final WalletDao walletDao;

    @Inject
    public WalletRepository(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public LiveData<List<Wallet>> getAllDataAsLiveData() {
        return walletDao.getAllAsLiveData();
    }

}
