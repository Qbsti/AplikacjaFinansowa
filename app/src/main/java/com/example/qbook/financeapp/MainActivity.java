package com.example.qbook.financeapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qbook.financeapp.Data.Lifecycle.ExpenseModel;
import com.example.qbook.financeapp.Data.Lifecycle.GoalModel;
import com.example.qbook.financeapp.Data.Lifecycle.IncomeModel;
import com.example.qbook.financeapp.Data.Lifecycle.WalletBalanceHistoryModel;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.WalletBalanceHistory;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.Inflater;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LifecycleOwner {

    private LifecycleRegistry lifecycleRegistry;

    private List<Expense> expenseList;
    private List<Income> incomeList;
    private List<WalletBalanceHistory> walletBalanceHistoryList;
    private List<Goal> goalList;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private GoalModel goalModel;
    private WalletBalanceHistoryModel walletBalanceHistoryModel;
    private ExpenseModel expenseModel;
    private IncomeModel incomeModel;

    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private LinearLayoutManager layoutManager;

    public MainActivity() {
    }

    public static MainActivity newInstance() {
        return new MainActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        ((FinanceAppApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        TextView balanceText = (TextView) findViewById(R.id.wallet_balance_value);

        GraphView graph = (GraphView) findViewById(R.id.balance_graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setDrawAsPath(true);
        series.setColor(getResources().getColor(R.color.colorPrimary));
        series.setDrawBackground(true);
        series.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLightTransparent));
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), dateFormat));
        graph.setTitle("Balance history:");
        graph.setTitleTextSize(60);

        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
        graph.getGridLabelRenderer().setTextSize(30);
        graph.getGridLabelRenderer().setHumanRounding(false);


        walletBalanceHistoryModel = ViewModelProviders.of(this, viewModelFactory)
                .get(WalletBalanceHistoryModel.class);
        expenseModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseModel.class);
        incomeModel = ViewModelProviders.of(this, viewModelFactory)
                .get(IncomeModel.class);
        goalModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GoalModel.class);

        walletBalanceHistoryModel.getAllAsLiveData().observe(this, new Observer<List<WalletBalanceHistory>>() {
            @Override
            public void onChanged(@Nullable List<WalletBalanceHistory> walletBalanceHistories) {
                walletBalanceHistoryList = walletBalanceHistories;
                if (walletBalanceHistories != null && walletBalanceHistories.size() > 0) {
                    if (walletBalanceHistories.size() < 6) {
                        graph.getGridLabelRenderer().setNumHorizontalLabels(walletBalanceHistories.size());
                    }
                    balanceText.setText(String.valueOf(walletBalanceHistories.get((walletBalanceHistories.size() - 1)).getWalletBalance()));
                    graph.removeAllSeries();

                    List<DataPoint> datapoints = new ArrayList<>();
                    for (WalletBalanceHistory entry : walletBalanceHistoryList) {
                        datapoints.add(new DataPoint(entry.getWalletBalanceDate(), entry.getWalletBalance()));
                    }
                    DataPoint[] data = datapoints.toArray(new DataPoint[0]);
                    series.resetData(data);
                    graph.getViewport().setMinY((series.getLowestValueY() < 0) ? series.getLowestValueY() : 0);
                    graph.getViewport().setMaxY(series.getHighestValueY() * 1.25);
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.addSeries(series);
                    graph.getViewport().setXAxisBoundsManual(true);
                }
            }
        });

        expenseModel.getAllAsLiveData().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                expenseList = expenses;
                updateTodaysBalanceHistory(expenseList, incomeList);
            }
        });

        incomeModel.getAllAsLiveData().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> incomes) {
                incomeList = incomes;
                updateTodaysBalanceHistory(expenseList, incomeList);
            }
        });

        goalModel.getAllAsLiveData().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable List<Goal> goals) {
                setListofGoals(goals);
                Log.d("1", goals != null ? goals.toString() : "goals=null");
            }
        });


        balanceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<WalletBalanceHistory> historyList = new ArrayList<>(buildTransactionHistory(expenseList, incomeList, walletBalanceHistoryList));

                walletBalanceHistoryModel.deleteAllGivenWalletBalanceHistory(walletBalanceHistoryList);
                walletBalanceHistoryModel.addNewWalletBalanceHistoryFromList(historyList);

            }
        });
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(MainActivity.this, String.valueOf(dataPoint.getY()), LENGTH_SHORT).show();
            }
        });

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                Intent intent;
                switch (menuItem.toString()) {
                    case "new expense":
                        intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                        startActivity(intent);
                        break;
                    case "new income":
                        intent = new Intent(MainActivity.this, AddIncomeActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_in_main_activity);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.goal_horizontal_recyclerview_item, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Goal currentItem = goalList.get(position);

            holder.circleProgressView.setValueAnimated(currentItem.getMoneySavedForGoal() * 100 / currentItem.getValue(), 750);


            holder.name.setText(currentItem.getName());

        }

        @Override
        public int getItemCount() {
            return (goalList == null) ? 0 : goalList.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private CircleProgressView circleProgressView;
            private TextView name;

            private ViewGroup container;

            public CustomViewHolder(View itemView) {
                super(itemView);
                this.circleProgressView = (CircleProgressView) itemView.findViewById(R.id.goal_recycle_horizontal_item_circleView);
                this.name = (TextView) itemView.findViewById(R.id.goal_recycle_horizontal_item_name_textView);
                this.container = (ViewGroup) itemView.findViewById(R.id.goal_horizontal_recyclerview_item_root_constraint);
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }

    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                0) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

        };
        return simpleItemTouchCallback;
    }

    public void setListofGoals(List<Goal> listOfGoals) {
        this.goalList = listOfGoals;

        layoutInflater = getLayoutInflater();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.main_activity_horizontal_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        Log.d("list of goals:", listOfGoals.toString());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.divider
                )
        );

        recyclerView.addItemDecoration(
                itemDecoration
        );


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private List<WalletBalanceHistory> buildTransactionHistory(List<Expense> expenses, List<Income> incomes, List<WalletBalanceHistory> balanceHistory) {
        SortedSet<Expense> sortedExpenses = new TreeSet<>();
        SortedSet<Income> sortedIncomes = new TreeSet<>();
        SortedSet<WalletBalanceHistory> sortedBalanceHistory = new TreeSet<>();
        sortedExpenses.addAll(expenses);
        sortedIncomes.addAll(incomes);
        sortedBalanceHistory.addAll(balanceHistory);

        SortedSet<WalletBalanceHistory> newBalanceHistorySet = new TreeSet<>();

        Date firstDate;
        Date firstExpenseDate;
        Date firstIncomeDate;
        if (!sortedExpenses.isEmpty() && !sortedIncomes.isEmpty()) {
            if (sortedExpenses.isEmpty()) {
                firstDate = sortedIncomes.first().getDate();
            } else {
                firstIncomeDate = sortedIncomes.first().getDate();
                firstExpenseDate = sortedExpenses.first().getDate();
                firstDate = (firstExpenseDate.before(firstIncomeDate)) ? firstExpenseDate : firstIncomeDate;
            }

            Date firstBalanceHistoryDate = sortedBalanceHistory.first().getWalletBalanceDate();

            Calendar tempCal = Calendar.getInstance();

            tempCal.setTime(firstDate);
            while (!DateUtils.isToday(tempCal.getTimeInMillis() - DateUtils.DAY_IN_MILLIS)) {
                List<Income> incomeListForGivenDay = new ArrayList<>();
                List<Expense> expensesListForGivenDay = new ArrayList<>();

                tempCal.get(Calendar.DATE);
                if (!expenses.isEmpty()) {
                    for (Expense exp : expenses) {
                        Calendar expenseDate = Calendar.getInstance();
                        expenseDate.setTime(exp.getDate());
                        if (tempCal.get(Calendar.DATE) == expenseDate.get(Calendar.DATE) &&
                                tempCal.get(Calendar.MONTH) == expenseDate.get(Calendar.MONTH) &&
                                tempCal.get(Calendar.YEAR) == expenseDate.get(Calendar.YEAR)) {
                            expensesListForGivenDay.add(exp);
                        }
                    }
                }
                if (!incomes.isEmpty()) {
                    for (Income inc : incomes) {
                        Calendar incomeDate = Calendar.getInstance();
                        incomeDate.setTime(inc.getDate());
                        if (tempCal.get(Calendar.DATE) == incomeDate.get(Calendar.DATE) &&
                                tempCal.get(Calendar.MONTH) == incomeDate.get(Calendar.MONTH) &&
                                tempCal.get(Calendar.YEAR) == incomeDate.get(Calendar.YEAR)) {
                            incomeListForGivenDay.add(inc);
                        }
                    }
                }

                Float changeForGivenDay = calculateChangeForAGivenDay(expensesListForGivenDay, incomeListForGivenDay);
                WalletBalanceHistory newBalanceHistory = new WalletBalanceHistory();
                newBalanceHistory.setWalletBalance(changeForGivenDay);
                newBalanceHistory.setWalletBalanceDate(tempCal.getTime());
                newBalanceHistorySet.add(newBalanceHistory);
                tempCal.add(Calendar.DATE, 1);
            }
            Log.d("tag", newBalanceHistorySet.toString());
        }
        float previousBalance = 0;

        List<WalletBalanceHistory> finalBalanceHistoryList = new ArrayList<>();
        Iterator<WalletBalanceHistory> iterator = newBalanceHistorySet.iterator();
        WalletBalanceHistory currentDayBalance;
        while (iterator.hasNext()) {
            WalletBalanceHistory currentDayChange = iterator.next();
            currentDayBalance = new WalletBalanceHistory();
            currentDayBalance.setWalletBalanceDate(currentDayChange.getWalletBalanceDate());
            currentDayBalance.setWalletBalance(previousBalance + currentDayChange.getWalletBalance());
            finalBalanceHistoryList.add(currentDayBalance);
            previousBalance = currentDayBalance.getWalletBalance();
            iterator.remove();
        }

        for (WalletBalanceHistory entry : newBalanceHistorySet) {
            currentDayBalance = new WalletBalanceHistory();

        }
        Log.d("tag", newBalanceHistorySet.toString());
        Log.d("tag", finalBalanceHistoryList.toString());

        return finalBalanceHistoryList;
    }

    private void updateTodaysBalanceHistory(List<Expense> expenses, List<Income> incomes) {
        if (expenses != null && incomes != null) {

            List<Expense> tempExpenseList = new ArrayList<>(expenses);
            List<Income> tempIncomeList = new ArrayList<>(incomes);
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();


            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date yesterdayEnd = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date yesterdayStart = cal.getTime();

            Optional<WalletBalanceHistory> todaysBalance = null;
            Optional<Float> yesterdayBalance = null;
            if (walletBalanceHistoryList != null) {
                todaysBalance = walletBalanceHistoryList.stream()
                        .filter(entry -> DateUtils.isToday(entry.getWalletBalanceDate().getTime()))
                        .findFirst();

                yesterdayBalance = walletBalanceHistoryList.stream()
                        .filter(entry -> entry.getWalletBalanceDate().after(yesterdayStart) && entry.getWalletBalanceDate().before(yesterdayEnd))
                        .map(WalletBalanceHistory::getWalletBalance)
                        .findFirst();

                buildTransactionHistory(tempExpenseList, tempIncomeList, walletBalanceHistoryList);
            }


            float balance = calculateChangeForAGivenDay(findTodaysExpensesFromList(tempExpenseList), findTodaysIncomeFromList(tempIncomeList));

            if (yesterdayBalance != null) {
                balance = balance + (yesterdayBalance.orElse(0.F));
            }

            if (walletBalanceHistoryList != null) {
                if (!todaysBalance.isPresent()) {
                    WalletBalanceHistory newTodaysBalance = new WalletBalanceHistory();
                    newTodaysBalance.setWalletBalance(balance);
                    newTodaysBalance.setWalletBalanceDate(now);
                    walletBalanceHistoryModel.addNewWalletBalanceHistory(newTodaysBalance);
                } else {
                    todaysBalance.get().setWalletBalance(balance);
                    walletBalanceHistoryModel.updateWalletBalanceHistory(todaysBalance.get());
                }

            }
        }
    }

    private List<Expense> findTodaysExpensesFromList(List<Expense> expenses) {
        if (expenses != null) {
            Iterator<Expense> iterator = expenses.iterator();
            while (iterator.hasNext()) {
                if (!DateUtils.isToday(iterator.next().getDate().getTime())) {
                    iterator.remove();
                }
            }
            return expenses;
        } else {
            return new ArrayList<>();
        }

    }

    private List<Income> findTodaysIncomeFromList(List<Income> incomes) {
        if (incomes != null) {
            Iterator<Income> iterator = incomes.iterator();
            while (iterator.hasNext()) {
                if (!DateUtils.isToday(iterator.next().getDate().getTime())) {
                    iterator.remove();
                }
            }
            return incomes;
        } else {
            return new ArrayList<>();
        }
    }

    private WalletBalanceHistory getBalanceEntryForGivenDay(AppDatabase database, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date midnight = cal.getTime();
        return database.walletBalanceHistoryDao().getGivenDateBalance(midnight, date);
    }

    private void updateBalanceHistory(Date date, AppDatabase database) {
        if (DateUtils.isToday(date.getTime())) {
        } else {
        }
    }

    private float calculateChangeForAGivenDay(List<Expense> expenses, List<Income> incomes) {
        float change = 0;
        for (Expense expense : expenses) {
            change -= expense.getValue();
        }
        for (Income income : incomes) {
            change += income.getValue();
        }
        return change;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reload) {
            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_main) {
        } else if (id == R.id.nav_goals) {
            intent = new Intent(MainActivity.this, GoalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (id == R.id.nav_budget_breakdown) {
            intent = new Intent(MainActivity.this, BudgetBreakdownActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
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
