package com.example.qbook.financeapp.Data.Repository;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.qbook.financeapp.Data.Lifecycle.ExpenseCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.ExpenseModel;
import com.example.qbook.financeapp.Data.Lifecycle.GoalModel;
import com.example.qbook.financeapp.Data.Lifecycle.IncomeCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.IncomeModel;
import com.example.qbook.financeapp.Data.Lifecycle.WalletBalanceHistoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.WalletModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final ExpenseRepository expenseRepository;
    private final GoalRepository goalRepository;
    private final IncomeRepository incomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final WalletBalanceHistoryRepository walletBalanceHistoryRepository;
    private final WalletRepository walletRepository;

    @Inject
    public CustomViewModelFactory(ExpenseRepository expenseRepository,
                                  GoalRepository goalRepository,
                                  IncomeRepository incomeRepository,
                                  IncomeCategoryRepository incomeCategoryRepository,
                                  ExpenseCategoryRepository expenseCategoryRepository,
                                  WalletBalanceHistoryRepository walletBalanceHistoryRepository, WalletRepository walletRepository) {
        this.expenseRepository = expenseRepository;
        this.goalRepository = goalRepository;
        this.incomeRepository = incomeRepository;
        this.incomeCategoryRepository = incomeCategoryRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.walletBalanceHistoryRepository = walletBalanceHistoryRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExpenseModel.class)) {
            return (T) new ExpenseModel(expenseRepository);
        } else if (modelClass.isAssignableFrom(ExpenseCategoryModel.class)) {
            return (T) new ExpenseCategoryModel(expenseCategoryRepository);
        } else if (modelClass.isAssignableFrom(GoalModel.class)) {
            return (T) new GoalModel(goalRepository);
        } else if (modelClass.isAssignableFrom(WalletModel.class)) {
            return (T) new WalletModel(walletRepository);
        } else if (modelClass.isAssignableFrom(IncomeModel.class)) {
            return (T) new IncomeModel(incomeRepository);
        } else if (modelClass.isAssignableFrom(IncomeCategoryModel.class)) {
            return (T) new IncomeCategoryModel(incomeCategoryRepository);
        } else if (modelClass.isAssignableFrom(WalletBalanceHistoryModel.class)) {
            return (T) new WalletBalanceHistoryModel(walletBalanceHistoryRepository);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
