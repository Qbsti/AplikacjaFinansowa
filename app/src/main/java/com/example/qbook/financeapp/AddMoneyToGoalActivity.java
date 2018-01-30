package com.example.qbook.financeapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qbook.financeapp.Data.Lifecycle.ExpenseCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.ExpenseModel;
import com.example.qbook.financeapp.Data.Lifecycle.GoalModel;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static android.widget.Toast.LENGTH_SHORT;

public class AddMoneyToGoalActivity extends AppCompatActivity implements LifecycleOwner {
    private static final String EXTRA_GOAL_ID = "EXTRA_GOAL_ID";

    private LifecycleRegistry lifecycleRegistry;

    private int goalId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    GoalModel goalModel;
    ExpenseModel expenseModel;
    ExpenseCategoryModel expenseCategoryModel;

    Goal goal;
    List<ExpenseCategory> expenseCategoryList;

    public AddMoneyToGoalActivity() {
    }

    public static AddMoneyToGoalActivity newInstance(String goalId) {
        return new AddMoneyToGoalActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_to_goal);
        setTitle("Allocate money");
        ((FinanceAppApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_GOAL_ID)) {
            this.goalId = intent.getIntExtra(EXTRA_GOAL_ID,0);
        }

        final TextView nameTextView = (TextView) findViewById(R.id.goal_add_money_name_textView);
        final TextView valueTextView = (TextView) findViewById(R.id.goal_add_money_value_textView);
        final TextView amountSavedTextView = (TextView) findViewById(R.id.goal_add_money_amount_saved_textView);
        final EditText howMuchToAddEditText = (EditText) findViewById(R.id.goal_add_money_how_much_editText);

        goalModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GoalModel.class);

        expenseModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseModel.class);

        goalModel.getGoalById(goalId).observe(this, new Observer<Goal>() {
            @Override
            public void onChanged(@Nullable Goal goalData) {
                goal = goalData;
                nameTextView.setText(goal.getName());
                valueTextView.setText(String.valueOf(goal.getValue()));
                amountSavedTextView.setText(String.valueOf(goal.getMoneySavedForGoal()));
            }
        });

        expenseCategoryModel= ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseCategoryModel.class);

        expenseCategoryModel.getAllAsLiveData().observe(this, new Observer<List<ExpenseCategory>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseCategory> expenseCategories) {
                expenseCategoryList=expenseCategories;
            }
        });

        final Button addFullButton = (Button) findViewById(R.id.goal_add_money_add_full_button);
        addFullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howMuchToAddEditText.setText(String.valueOf(goal.getValue() - goal.getMoneySavedForGoal()));
            }
        });

        final Button addMoneyButton = (Button) findViewById(R.id.goal_confirm_add_money_button);
        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float moneysaved = goal.getMoneySavedForGoal();
                float value = goal.getValue();
                float difference = value-moneysaved;
                float howMuchtoAdd = Float.valueOf(String.valueOf(howMuchToAddEditText.getText()));
                if(howMuchtoAdd<=difference) {
                    goal.setMoneySavedForGoal(moneysaved + Float.valueOf(String.valueOf(howMuchToAddEditText.getText())));
                    if(howMuchtoAdd==difference) {
                        goal.setDone(true);
                        Expense newExpense = new Expense();
                        newExpense.setName(goal.getName());
                        newExpense.setCategoryName("Goals");
                        if(expenseCategoryList.stream().noneMatch(category->category.getName().equals("Goals"))) {
                            expenseCategoryModel.addNewExpenseCategory(new ExpenseCategory("Goals"));
                        }
                        newExpense.setDate(new Date());
                        newExpense.setValue(value);
                        expenseModel.addNewExpense(newExpense);
                    }
                    goalModel.updateGoal(goal);
                    finish();
                } else {
                    Toast.makeText(AddMoneyToGoalActivity.this, "Provided value was to big (max: " + difference + " )", LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        lifecycleRegistry
                .markState(Lifecycle.State.STARTED);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED);

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}

