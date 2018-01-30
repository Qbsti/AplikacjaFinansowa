package com.example.qbook.financeapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.TypeConverter;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qbook.financeapp.Data.Lifecycle.ExpenseCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.ExpenseModel;
import com.example.qbook.financeapp.Data.RecurrenceType;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.EnumConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class AddExpenseActivity extends AppCompatActivity implements LifecycleOwner {
    private LifecycleRegistry lifecycleRegistry;

    boolean recurrentCheckBoxSelected = false;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ExpenseModel expenseModel;
    ExpenseCategoryModel expenseCategoryModel;


    public AddExpenseActivity() {
    }

    public static AddExpenseActivity newInstance() {
        return new AddExpenseActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle("Add new expense");

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        ((FinanceAppApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        expenseModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseModel.class);

        expenseCategoryModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseCategoryModel.class);

        expenseCategoryModel.getAllAsLiveData().observe(this, new Observer<List<ExpenseCategory>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseCategory> expenseCategories) {
                populateCategorySpinner(expenseCategories);
            }
        });

        final CheckBox recurrentCheckBox = (CheckBox) findViewById(R.id.recurrent_expense_check_box);
        final SeekBar recurrencySeekBar = (SeekBar) findViewById(R.id.expense_recurrency_seekbar);
        final TextView recurrencySeekBarDescription = (TextView) findViewById(R.id.expense_recurrency_seekbar_description);
        final Button saveExpenseButton = (Button) findViewById(R.id.save_expense_button);
        final Button addExpenseCategoryButton = (Button) findViewById(R.id.add_expense_category_button);



        addExpenseCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpenseCategory();
            }
        });

        saveExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewExpense(view);
            }
        });


        recurrencySeekBar.setEnabled(recurrentCheckBoxSelected);
        recurrencySeekBarDescription.setVisibility(View.INVISIBLE);

        recurrentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                recurrentCheckBoxSelected ^= true;
                recurrencySeekBar.setEnabled(recurrentCheckBoxSelected);
                setRecurrentCheckBoxDescription(recurrencySeekBar.getProgress());
                recurrencySeekBarDescription.setVisibility((recurrentCheckBoxSelected) ? View.VISIBLE : View.INVISIBLE);
            }
        });

        recurrencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setRecurrentCheckBoxDescription(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(AddExpenseActivity.this,String.valueOf(recurrencySeekBar.getProgress()),Toast.LENGTH_LONG).show();
            }
        });


    }

    void populateCategorySpinner(List<ExpenseCategory> expenseCategoriesList) {
        Spinner spinner = (Spinner) findViewById(R.id.expense_category_spinner);

        List<String> expenseCategoriesNamesList = new ArrayList<>();

        if (expenseCategoriesList != null) {
            for (ExpenseCategory expenseCategory : expenseCategoriesList) {
                expenseCategoriesNamesList.add(expenseCategory.getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddExpenseActivity.this, android.R.layout.simple_spinner_dropdown_item, expenseCategoriesNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void saveNewExpense(View view) {
        final EditText expenseTitle = (EditText) findViewById(R.id.expense_title_input_text);
        final Spinner expenseCategorySpinner = (Spinner) findViewById(R.id.expense_category_spinner);
        final EditText expenseValue = (EditText) findViewById(R.id.expense_value_input_text);
        final SeekBar expenseRecurrencySeekbar = (SeekBar) findViewById(R.id.expense_recurrency_seekbar);

        Expense newExpense = new Expense();
        newExpense.setName(expenseTitle.getText().toString());

        newExpense.setCategoryName(expenseCategorySpinner.getSelectedItem().toString());
        newExpense.setDate(new Date());
        newExpense.setValue(Float.valueOf(expenseValue.getText().toString()));
        expenseModel.addNewExpense(newExpense);
        Toast.makeText(AddExpenseActivity.this,newExpense.toString(),Toast.LENGTH_LONG).show();
        finish();
    }

    void addNewExpenseCategory() {
        final EditText expenseCategoryTextInput = (EditText) findViewById(R.id.new_expense_category_input_text);
        expenseCategoryModel.addNewExpenseCategory(new ExpenseCategory(expenseCategoryTextInput.getText().toString()));
        expenseCategoryTextInput.getText().clear();
    }

    void setRecurrentCheckBoxDescription(int i) {
        final TextView recurrencySeekBarDescription = (TextView) findViewById(R.id.expense_recurrency_seekbar_description);

        switch (i) {
            case 0:
                recurrencySeekBarDescription.setText("daily");
                break;
            case 1:
                recurrencySeekBarDescription.setText("weekly");
                break;
            case 2:
                recurrencySeekBarDescription.setText("every 2 weeks");
                break;
            case 3:
                recurrencySeekBarDescription.setText("monthly");
                break;
            case 4:
                recurrencySeekBarDescription.setText("every quarter");
                break;
            case 5:
                recurrencySeekBarDescription.setText("every 4 months");
                break;
            case 6:
                recurrencySeekBarDescription.setText("every 6 months");
                break;
            case 7:
                recurrencySeekBarDescription.setText("yearly");
                break;
        }
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
