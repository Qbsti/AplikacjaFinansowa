package com.example.qbook.financeapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qbook.financeapp.Data.Lifecycle.ExpenseCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.ExpenseModel;
import com.example.qbook.financeapp.Data.Lifecycle.GoalModel;
import com.example.qbook.financeapp.Data.Lifecycle.IncomeCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.IncomeModel;
import com.example.qbook.financeapp.Data.Lifecycle.WalletBalanceHistoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.WalletModel;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Wallet;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.WalletBalanceHistory;

import java.io.File;
import java.io.FileOutputStream;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import static android.widget.Toast.LENGTH_SHORT;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LifecycleOwner {

    private LifecycleRegistry lifecycleRegistry;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    GoalModel goalModel;
    ExpenseModel expenseModel;
    ExpenseCategoryModel expenseCategoryModel;
    IncomeModel incomeModel;
    IncomeCategoryModel incomeCategoryModel;
    WalletBalanceHistoryModel walletBalanceHistoryModel;
    WalletModel walletModel;

    private List<Goal> goalList;
    private List<Expense> expenseList;
    private List<ExpenseCategory> expenseCategoriesList;
    private List<Income> incomeList;
    private List<IncomeCategory> incomeCategoryList;
    private List<WalletBalanceHistory> walletBalanceHistoryList;
    private List<Wallet> walletList;

    public SettingsActivity() {
    }

    public static SettingsActivity newInstance() {
        return new SettingsActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        ((FinanceAppApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        goalModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GoalModel.class);

        goalModel.getAllAsLiveData().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable List<Goal> goals) {
                goalList = goals;
            }
        });

        expenseModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseModel.class);

        expenseModel.getAllAsLiveData().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                expenseList = expenses;
            }
        });

        expenseCategoryModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseCategoryModel.class);

        expenseCategoryModel.getAllAsLiveData().observe(this, new Observer<List<ExpenseCategory>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseCategory> expenseCategories) {
                expenseCategoriesList = expenseCategories;
            }
        });

        incomeModel = ViewModelProviders.of(this, viewModelFactory)
                .get(IncomeModel.class);

        incomeModel.getAllAsLiveData().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> incomes) {
                incomeList = incomes;
            }
        });

        incomeCategoryModel = ViewModelProviders.of(this, viewModelFactory)
                .get(IncomeCategoryModel.class);

        incomeCategoryModel.getAllAsLiveData().observe(this, new Observer<List<IncomeCategory>>() {
            @Override
            public void onChanged(@Nullable List<IncomeCategory> incomeCategories) {
                incomeCategoryList = incomeCategories;
            }
        });

        walletBalanceHistoryModel = ViewModelProviders.of(this, viewModelFactory)
                .get(WalletBalanceHistoryModel.class);

        walletBalanceHistoryModel.getAllAsLiveData().observe(this, new Observer<List<WalletBalanceHistory>>() {
            @Override
            public void onChanged(@Nullable List<WalletBalanceHistory> walletBalanceHistories) {
                walletBalanceHistoryList = walletBalanceHistories;
            }
        });

        walletModel = ViewModelProviders.of(this, viewModelFactory)
                .get(WalletModel.class);

        walletModel.getAllDataAsLiveData().observe(this, new Observer<List<Wallet>>() {
            @Override
            public void onChanged(@Nullable List<Wallet> wallets) {
                walletList = wallets;
            }
        });

        final Button exportDataToTxtButton = (Button) findViewById(R.id.export_to_txt_button);
        final Button exportDataToCsvButton = (Button) findViewById(R.id.export_to_csv_button);

        exportDataToTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String output = parseDatabaseStringsForTxt();
                exportDataToFile(output, ".txt");
            }
        });

        exportDataToCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String output = parseDatabaseStringsForCsv();
                exportDataToFile(output, ".csv");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_in_settings_activity);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void exportDataToFile(String data, String extension) {
        String output = data;
        String state = Environment.getExternalStorageState();

        Calendar calendar = Calendar.getInstance();
        String currentDateString = String.valueOf(android.text.format.DateFormat.format("yyyy.MM.dd HH:mm:ss", calendar.getTime()));

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File file = new File(getApplicationContext().getExternalFilesDir(
                null), currentDateString + extension);

        FileOutputStream outputStream;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(output.getBytes());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(SettingsActivity.this, "Data saved to: " + file.getPath(), LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String parseDatabaseStringsForCsv() {
        String output = "sep=,";

        output = output + System.lineSeparator() +
                "FinanceApp exported csv data: " +
                System.lineSeparator() +
                ParseWalletDataCsv() +
                ParseExpenseCategoryDataCsv() +
                ParseExpenseDataCsv() +
                ParseIncomeCategoriesDataCsv() +
                ParseIncomeDataCsv() +
                ParseGoalsDataCsv() +
                ParseWalletBalanceHistoryDataCsv();
        return output;
    }


    String parseDatabaseStringsForTxt() {
        String output = "FinanceApp exported txt data: ";

        output = output + System.lineSeparator() +
                ParseWalletDataTxt() +
                ParseExpenseCategoryDataTxt() +
                ParseExpenseDataTxt() +
                ParseIncomeCategoriesDataTxt() +
                ParseIncomeDataTxt() +
                ParseGoalsDataTxt() +
                ParseWalletBalanceHistoryDataTxt();
        return output;
    }

    String ParseWalletDataTxt() {
        String output = "Wallets: ";
        List<Wallet> wallets =walletList;

        StringBuilder outputBuilder = new StringBuilder(output);
        for (Wallet w : wallets) {

            outputBuilder.append(System.lineSeparator())
                    .append(w.toString());
        }
        outputBuilder.append("; ").append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseExpenseDataTxt() {
        String output = "Expenses: ";
        List<Expense> expenses = expenseList;

        StringBuilder outputBuilder = new StringBuilder(output);
        if (expenses != null) {
            for (Expense e : expenses) {

                outputBuilder.append(System.lineSeparator()).append(e.toString());
            }
        }
        outputBuilder.append("; ").append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseIncomeDataTxt() {
        String output = "Incomes: ";
        List<Income> incomes = incomeList;

        StringBuilder outputBuilder = new StringBuilder(output);
        if (incomes != null) {
            for (Income i : incomes) {

                outputBuilder.append(System.lineSeparator()).append(i.toString());
            }
        }
        outputBuilder.append("; ").append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseExpenseCategoryDataTxt() {
        String output = "Expense Categories: ";
        List<ExpenseCategory> expenseCategories = expenseCategoriesList;

        StringBuilder outputBuilder = new StringBuilder(output);
        if (expenseCategories != null) {
            for (ExpenseCategory e : expenseCategories) {

                outputBuilder.append(System.lineSeparator()).append(e.toString());
            }
        }
        outputBuilder.append("; ").append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseIncomeCategoriesDataTxt() {
        String output = "Income Categories: ";
        List<IncomeCategory> incomeCategories = incomeCategoryList;

        StringBuilder outputBuilder = new StringBuilder(output);
        if (incomeCategories != null) {
            for (IncomeCategory i : incomeCategories) {

                outputBuilder.append(System.lineSeparator()).append(i.toString());
            }
        }
        outputBuilder.append("; ").append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseGoalsDataTxt() {
        String output = "Goals: ";
        List<Goal> goals = goalList;

        StringBuilder outputBuilder = new StringBuilder(output);
        if (goals != null) {
            for (Goal g : goals) {

                outputBuilder.append(System.lineSeparator()).append(g.toString());
            }
        }
        outputBuilder.append("; ").append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseWalletBalanceHistoryDataTxt() {
        String output = "Wallet Balance History: ";
        List<WalletBalanceHistory> walletBalance = walletBalanceHistoryList;

        StringBuilder outputBuilder = new StringBuilder(output);
        if (walletBalance != null) {
            for (WalletBalanceHistory w : walletBalance) {
                outputBuilder.append(System.lineSeparator()).append(w.toString());
            }
        }
        outputBuilder.append("; ").append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseWalletDataCsv() {
        String output = "Wallets: ";
        List<Wallet> wallets = walletList;

        StringBuilder outputBuilder = new StringBuilder(output);
        outputBuilder.append(System.lineSeparator())
                .append("Currency ");
        for (Wallet w : wallets) {
            outputBuilder.append(System.lineSeparator())
                    .append(w.getCurrency());
        }
        outputBuilder.append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseExpenseDataCsv() {
        String output = "Expenses: ";
        List<Expense> expenses = expenseList;

        StringBuilder outputBuilder = new StringBuilder(output);
        outputBuilder.append(System.lineSeparator())
                .append("Name, ")
                .append("Category, ")
                .append("Value, ")
                .append("Date ");
        if (expenses != null) {
            for (Expense e : expenses) {
                outputBuilder.append(System.lineSeparator())
                        .append(e.getName())
                        .append(",")
                        .append(e.getCategoryName())
                        .append(",")
                        .append(e.getValue())
                        .append(",")
                        .append(e.getDate());
            }
        }
        outputBuilder.append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseIncomeDataCsv() {
        String output = "Incomes: ";
        List<Income> incomes = incomeList;

        StringBuilder outputBuilder = new StringBuilder(output);
        outputBuilder.append(System.lineSeparator())
                .append("Name, ")
                .append("Category, ")
                .append("Value, ")
                .append("Date ");
        if (incomes != null) {
            for (Income i : incomes) {
                outputBuilder.append(System.lineSeparator())
                        .append(i.getName())
                        .append(",")
                        .append(i.getCategoryName())
                        .append(",")
                        .append(i.getValue())
                        .append(",")
                        .append(i.getDate());
            }
        }
        outputBuilder.append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseExpenseCategoryDataCsv() {
        String output = "Expense Categories: ";
        List<ExpenseCategory> expenseCategories = expenseCategoriesList;

        StringBuilder outputBuilder = new StringBuilder(output);
        outputBuilder.append(System.lineSeparator())
                .append("Name ");
        if (expenseCategories != null) {
            for (ExpenseCategory e : expenseCategories) {
                outputBuilder.append(System.lineSeparator()).append(e.getName());
            }
        }
        outputBuilder.append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseIncomeCategoriesDataCsv() {
        String output = "Income Categories: ";
        List<IncomeCategory> incomeCategories = incomeCategoryList;

        StringBuilder outputBuilder = new StringBuilder(output);
        outputBuilder.append(System.lineSeparator())
                .append("Name ");
        if (incomeCategories != null) {
            for (IncomeCategory i : incomeCategories) {
                outputBuilder.append(System.lineSeparator()).append(i.getName());
            }
        }
        outputBuilder.append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseGoalsDataCsv() {
        String output = "Goals: ";
        List<Goal> goals = goalList;

        StringBuilder outputBuilder = new StringBuilder(output);
        outputBuilder.append(System.lineSeparator())
                .append("Name, ")
                .append("Value, ")
                .append("Money saved for goal ");
        if (goals != null) {
            for (Goal g : goals) {
                outputBuilder.append(System.lineSeparator())
                        .append(g.getName())
                        .append(",")
                        .append(g.getValue())
                        .append(",")
                        .append(g.getMoneySavedForGoal());
            }
        }
        outputBuilder.append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    String ParseWalletBalanceHistoryDataCsv() {
        String output = "Wallet Balance History: ";
        List<WalletBalanceHistory> walletBalance = walletBalanceHistoryList;

        StringBuilder outputBuilder = new StringBuilder(output);
        outputBuilder.append(System.lineSeparator())
                .append("Wallet id, ")
                .append("Balance, ")
                .append("BalanceDate ");
        if (walletBalance != null) {
            for (WalletBalanceHistory w : walletBalance) {
                outputBuilder.append(System.lineSeparator())
                        .append(w.getWalletId())
                        .append(",")
                        .append(w.getWalletBalance())
                        .append(",")
                        .append(w.getWalletBalanceDate());
            }
        }
        outputBuilder.append(System.lineSeparator());
        output = outputBuilder.toString();

        return output;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_main) {
            intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_goals) {
            intent = new Intent(SettingsActivity.this, GoalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_budget_breakdown) {
            intent = new Intent(SettingsActivity.this, BudgetBreakdownActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
