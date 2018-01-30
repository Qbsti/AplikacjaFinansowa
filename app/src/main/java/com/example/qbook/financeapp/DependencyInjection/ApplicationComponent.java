package com.example.qbook.financeapp.DependencyInjection;

import android.app.Application;

import com.example.qbook.financeapp.AddExpenseActivity;
import com.example.qbook.financeapp.AddGoalActivity;
import com.example.qbook.financeapp.AddIncomeActivity;
import com.example.qbook.financeapp.AddMoneyToGoalActivity;
import com.example.qbook.financeapp.BudgetBreakdownActivity;
import com.example.qbook.financeapp.BudgetBreakdownExpensesFragment;
import com.example.qbook.financeapp.BudgetBreakdownIncomesFragment;
import com.example.qbook.financeapp.GoalActivity;
import com.example.qbook.financeapp.GoalFragment;
import com.example.qbook.financeapp.MainActivity;
import com.example.qbook.financeapp.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RoomModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(GoalActivity goalActivity);
    void inject(BudgetBreakdownActivity budgetBreakdownActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(BudgetBreakdownExpensesFragment budgetBreakdownExpensesFragment);
    void inject(BudgetBreakdownIncomesFragment budgetBreakdownIncomesFragment);
    void inject(AddGoalActivity addGoalActivity);
    void inject(AddExpenseActivity addExpenseActivity);
    void inject(AddIncomeActivity addIncomeActivity);
    void inject(GoalFragment goalFragment);
    void inject(AddMoneyToGoalActivity addMoneyToGoalActivity);
    Application application();


}
