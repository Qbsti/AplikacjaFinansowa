package com.example.qbook.financeapp.Data.Lifecycle;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.qbook.financeapp.Data.Repository.WalletRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Wallet;

import java.util.List;

public class WalletModel extends ViewModel {
    private WalletRepository walletRepository;

    public WalletModel(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public LiveData<List<Wallet>> getAllDataAsLiveData() {
        return walletRepository.getAllDataAsLiveData();
    }
}
