package com.example.qbook.financeapp.Data.Lifecycle;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.qbook.financeapp.Data.Repository.WalletBalanceHistoryRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.WalletBalanceHistory;

import java.util.List;

public class WalletBalanceHistoryModel extends ViewModel {
    private WalletBalanceHistoryRepository walletBalanceHistoryRepository;

    public WalletBalanceHistoryModel(WalletBalanceHistoryRepository walletBalanceHistoryRepository) {
        this.walletBalanceHistoryRepository = walletBalanceHistoryRepository;
    }

    public List<WalletBalanceHistory> getAllData() {
        return walletBalanceHistoryRepository.getAllData();
    }

    public LiveData<List<WalletBalanceHistory>> getAllAsLiveData() {
        return walletBalanceHistoryRepository.getAllAsLiveData();
    }

    public void deleteAllGivenWalletBalanceHistory(List<WalletBalanceHistory> walletBalanceHistories) {
        DeleteAllGivenWalletBalanceHistoryTask task = new DeleteAllGivenWalletBalanceHistoryTask();
        task.execute(walletBalanceHistories);
    }

    private class DeleteAllGivenWalletBalanceHistoryTask extends AsyncTask<List<WalletBalanceHistory>, Void, Void> {

        @Override
        protected Void doInBackground(List<WalletBalanceHistory>[] lists) {
            lists[0].forEach(entry-> walletBalanceHistoryRepository.deleteWalletBallanceHistory(entry));
            return null;
        }
    }

    public void deleteWalletBalanceHistory(WalletBalanceHistory walletBalanceHistory) {
        DeleteWalletBalanceHistoryTask task = new DeleteWalletBalanceHistoryTask();
        task.execute(walletBalanceHistory);
    }

    private class DeleteWalletBalanceHistoryTask extends AsyncTask<WalletBalanceHistory, Void, Void> {
        @Override
        protected Void doInBackground(WalletBalanceHistory... walletBalanceHistories) {
            walletBalanceHistoryRepository.deleteWalletBallanceHistory(walletBalanceHistories[0]);
            return null;
        }
    }

    public void eraseAllWalletBalanceHistory() {
        EraseAllWalletBalanceHistoryTask task = new EraseAllWalletBalanceHistoryTask();
        task.execute();
    }

    private class EraseAllWalletBalanceHistoryTask extends AsyncTask<Void,Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            walletBalanceHistoryRepository.eraseAll();
            return null;
        }
    }

    public void addNewWalletBalanceHistoryFromList(List<WalletBalanceHistory> walletBalanceHistory){
        new AddNewWalletBalanceHistoryFromListTask().execute(walletBalanceHistory);
    }

    private class AddNewWalletBalanceHistoryFromListTask extends AsyncTask<List<WalletBalanceHistory>, Void, Void> {


        @Override
        protected Void doInBackground(List<WalletBalanceHistory>[] lists) {
            walletBalanceHistoryRepository.saveWalletBalanceHistoryFromList(lists[0]);
            return null;
        }
    }

    public void addNewWalletBalanceHistory(WalletBalanceHistory walletBalanceHistory){
        new AddNewWalletBalanceHistoryTask().execute(walletBalanceHistory);
    }

    private class AddNewWalletBalanceHistoryTask extends AsyncTask<WalletBalanceHistory, Void, Void> {

        @Override
        protected Void doInBackground(WalletBalanceHistory... walletBalanceHistories) {
            walletBalanceHistoryRepository.saveWalletBalanceHistory(walletBalanceHistories);
            return null;
        }
    }

    public void updateWalletBalanceHistory(WalletBalanceHistory walletBalanceHistory){
        new updateWalletBalanceHistoryTask().execute(walletBalanceHistory);
    }

    private class updateWalletBalanceHistoryTask extends AsyncTask<WalletBalanceHistory, Void, Void> {

        @Override
        protected Void doInBackground(WalletBalanceHistory... walletBalanceHistories) {
            walletBalanceHistoryRepository.updateWalletBalanceHistory(walletBalanceHistories);
            return null;
        }
    }

}
