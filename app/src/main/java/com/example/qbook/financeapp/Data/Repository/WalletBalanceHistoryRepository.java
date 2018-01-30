package com.example.qbook.financeapp.Data.Repository;

import android.arch.lifecycle.LiveData;

import com.example.qbook.financeapp.Data.RoomDatabase.DAO.WalletBalanceHistoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.WalletBalanceHistory;

import java.util.List;

import javax.inject.Inject;

public class WalletBalanceHistoryRepository {
    private final WalletBalanceHistoryDao walletBalanceHistoryDao;

    @Inject
    public WalletBalanceHistoryRepository(WalletBalanceHistoryDao walletBalanceHistoryDao) {
        this.walletBalanceHistoryDao = walletBalanceHistoryDao;
    }

    public List<WalletBalanceHistory> getAllData() {
        return walletBalanceHistoryDao.getAll();
    }

    public LiveData<List<WalletBalanceHistory>> getAllAsLiveData() {
        return walletBalanceHistoryDao.getAllAsLiveData();
    }

    public void deleteWalletBallanceHistory(WalletBalanceHistory walletBalanceHistory) {
        walletBalanceHistoryDao.deleteWalletBalanceHistory(walletBalanceHistory);
    }

    public void saveWalletBalanceHistory(WalletBalanceHistory... walletBalanceHistories) {
        walletBalanceHistoryDao.insertAll(walletBalanceHistories);
    }

    public void saveWalletBalanceHistoryFromList(List<WalletBalanceHistory> walletBalanceHistories) {
        walletBalanceHistoryDao.insertAllFromList(walletBalanceHistories);
    }

    public void updateWalletBalanceHistory(WalletBalanceHistory... walletBalanceHistories) {
        walletBalanceHistoryDao.updateBalanceEntries(walletBalanceHistories);
    }

    public void eraseAll() {
        walletBalanceHistoryDao.eraseAll();
    }

    public void deleteAllGiven(WalletBalanceHistory... walletBalanceHistories) {
        walletBalanceHistoryDao.deleteAllGivenHistory();
    }

}
