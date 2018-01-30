package com.example.qbook.financeapp.DependencyInjection;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.example.qbook.financeapp.Data.Repository.CustomViewModelFactory;
import com.example.qbook.financeapp.Data.Repository.ExpenseCategoryRepository;
import com.example.qbook.financeapp.Data.Repository.ExpenseRepository;
import com.example.qbook.financeapp.Data.Repository.GoalRepository;
import com.example.qbook.financeapp.Data.Repository.IncomeCategoryRepository;
import com.example.qbook.financeapp.Data.Repository.IncomeRepository;
import com.example.qbook.financeapp.Data.Repository.WalletBalanceHistoryRepository;
import com.example.qbook.financeapp.Data.Repository.WalletRepository;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseCategoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.GoalDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.IncomeCategoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.IncomeDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.WalletBalanceHistoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.WalletDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private final AppDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                AppDatabase.class,
                "ListItem.db"
        ).build();
    }

    @Provides
    @Singleton
    ExpenseRepository provideExpenseRepository(ExpenseDao expenseDao){
        return new ExpenseRepository(expenseDao);
    }

    @Provides
    @Singleton
    ExpenseDao provideExpenseDao(AppDatabase database){
        return database.expenseDao();
    }

    @Provides
    @Singleton
    GoalRepository provideGoalRepository(GoalDao goalDao){
        return new GoalRepository(goalDao);
    }

    @Provides
    @Singleton
    GoalDao provideGoalDao(AppDatabase database){
        return database.goalDao();
    }

    @Provides
    @Singleton
    IncomeRepository provideIncomeRepository(IncomeDao incomeDao){
        return new IncomeRepository(incomeDao);
    }

    @Provides
    @Singleton
    IncomeDao provideIncomeDao(AppDatabase database){
        return database.incomeDao();
    }

    @Provides
    @Singleton
    ExpenseCategoryRepository provideExpenseCategoryRepository(ExpenseCategoryDao expenseCategoryDao){
        return new ExpenseCategoryRepository(expenseCategoryDao);
    }

    @Provides
    @Singleton
    ExpenseCategoryDao provideExpenseCategoryDao(AppDatabase database){
        return database.expenseCategoryDao();
    }

    @Provides
    @Singleton
    IncomeCategoryRepository provideIncomeCategoryRepository(IncomeCategoryDao incomeCategoryDao){
        return new IncomeCategoryRepository(incomeCategoryDao);
    }

    @Provides
    @Singleton
    IncomeCategoryDao provideIncomeCategoryDao(AppDatabase database){
        return database.incomeCategoryDao();
    }

    @Provides
    @Singleton
    WalletBalanceHistoryRepository provideWalletBalanceHistoryRepository(WalletBalanceHistoryDao walletBalanceHistoryDao){
        return new WalletBalanceHistoryRepository(walletBalanceHistoryDao);
    }

    @Provides
    @Singleton
    WalletBalanceHistoryDao provideWalletBalanceHistoryDao(AppDatabase database){
        return database.walletBalanceHistoryDao();
    }

    @Provides
    @Singleton
    WalletRepository provideWalletRepository(WalletDao walletDao){
        return new WalletRepository(walletDao);
    }

    @Provides
    @Singleton
    WalletDao provideWalletDao(AppDatabase database){
        return database.walletDao();
    }

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application){
        return database;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(ExpenseRepository expenseRepository,
                                                      GoalRepository goalRepository,
                                                      IncomeCategoryRepository incomeCategoryRepository,
                                                      IncomeRepository incomeRepository,
                                                      ExpenseCategoryRepository expenseCategoryRepository,
                                                      WalletBalanceHistoryRepository walletBalanceHistoryRepository,
                                                      WalletRepository walletRepository){
        return new CustomViewModelFactory(expenseRepository,
                goalRepository,
                incomeRepository,
                incomeCategoryRepository,
                expenseCategoryRepository,
                walletBalanceHistoryRepository,
                walletRepository);
    }

}
