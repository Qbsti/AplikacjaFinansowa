package com.example.qbook.financeapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
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

import com.example.qbook.financeapp.Data.Lifecycle.IncomeCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.IncomeModel;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class AddIncomeActivity extends AppCompatActivity implements LifecycleOwner {
    private LifecycleRegistry lifecycleRegistry;

    boolean recurrentCheckBoxSelected = false;


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    IncomeModel incomeModel;
    IncomeCategoryModel incomeCategoryModel;


    public AddIncomeActivity() {
    }

    public static AddIncomeActivity newInstance() {
        return new AddIncomeActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        setTitle("Add new income");

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        ((FinanceAppApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        incomeModel = ViewModelProviders.of(this, viewModelFactory)
                .get(IncomeModel.class);

        incomeCategoryModel = ViewModelProviders.of(this, viewModelFactory)
                .get(IncomeCategoryModel.class);

        incomeCategoryModel.getAllAsLiveData().observe(this, new Observer<List<IncomeCategory>>() {
            @Override
            public void onChanged(@Nullable List<IncomeCategory> incomeCategories) {
                populateCategorySpinner(incomeCategories);
            }
        });

        final CheckBox recurrentCheckBox = (CheckBox) findViewById(R.id.recurrent_income_check_box);
        final SeekBar recurrencySeekBar = (SeekBar) findViewById(R.id.income_recurrency_seekbar);
        final TextView recurrencySeekBarDescription = (TextView) findViewById(R.id.income_recurrency_seekbar_description);
        final Button saveIncomeButton = (Button) findViewById(R.id.save_income_button);
        final Button addIncomeCategoryButton = (Button) findViewById(R.id.add_income_category_button);



        addIncomeCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewIncomeCategory();
            }
        });

        saveIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewIncome(view);
            }
        });


        recurrencySeekBar.setEnabled(recurrentCheckBoxSelected);
        recurrencySeekBarDescription.setVisibility(View.INVISIBLE);

        recurrentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                recurrentCheckBoxSelected ^= true;
                recurrencySeekBar.setEnabled(recurrentCheckBoxSelected);
                setIncomeCheckBoxDescription(recurrencySeekBar.getProgress());
                recurrencySeekBarDescription.setVisibility((recurrentCheckBoxSelected) ? View.VISIBLE : View.INVISIBLE);
            }
        });

        recurrencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setIncomeCheckBoxDescription(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    void populateCategorySpinner(List<IncomeCategory> incomeCategoryList) {
        Spinner spinner = (Spinner) findViewById(R.id.income_category_spinner);

        List<String> incomeCategoriesNamesList = new ArrayList<>();


        if (incomeCategoryList != null) {
            for (IncomeCategory incomeCategory : incomeCategoryList) {
                incomeCategoriesNamesList.add(incomeCategory.getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddIncomeActivity.this, android.R.layout.simple_spinner_dropdown_item, incomeCategoriesNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void saveNewIncome(View view) {
        final EditText incomeTitle = (EditText) findViewById(R.id.income_title_input_text);
        final Spinner incomeCategorySpinner = (Spinner) findViewById(R.id.income_category_spinner);
        final EditText incomeValue = (EditText) findViewById(R.id.income_value_input_text);
        final SeekBar incomeRecurrencySeekBar = (SeekBar) findViewById(R.id.income_recurrency_seekbar);

        Income newIncome = new Income();
        newIncome.setName(incomeTitle.getText().toString());


        newIncome.setCategoryName(incomeCategorySpinner.getSelectedItem().toString());
        newIncome.setDate(new Date());
        newIncome.setValue(Float.valueOf(incomeValue.getText().toString()));
        incomeModel.addNewIncome(newIncome);
        Toast.makeText(AddIncomeActivity.this, newIncome.toString(), Toast.LENGTH_LONG).show();
        finish();
    }

    void addNewIncomeCategory() {
        final EditText incomeCategoryTextInput = (EditText) findViewById(R.id.new_income_category_input_text);
incomeCategoryModel.addNewIncomeCategory(new IncomeCategory(incomeCategoryTextInput.getText().toString()));
        incomeCategoryTextInput.getText().clear();
    }

    void setIncomeCheckBoxDescription(int i) {
        final TextView recurrencySeekBarDescription = (TextView) findViewById(R.id.income_recurrency_seekbar_description);

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
