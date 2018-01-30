package com.example.qbook.financeapp;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qbook.financeapp.Data.Lifecycle.ExpenseCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.ExpenseModel;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;

public class BudgetBreakdownExpensesFragment extends Fragment implements OnChartValueSelectedListener, LifecycleOwner {

    private PieChart mChart;
    private LifecycleRegistry lifecycleRegistry;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ExpenseModel expenseModel;
    ExpenseCategoryModel expenseCategoryModel;

    List<Expense> expenseList;
    List<ExpenseCategory> expenseCategoryList;
    List<PieEntry> entries = new ArrayList<>();

    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private LinearLayoutManager layoutManager;

    public BudgetBreakdownExpensesFragment() {
    }

    public static BudgetBreakdownExpensesFragment newInstance() {
        return new BudgetBreakdownExpensesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        ((FinanceAppApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        expenseModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseModel.class);
        expenseCategoryModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseCategoryModel.class);


        expenseCategoryModel.getAllAsLiveData().observe(this, new Observer<List<ExpenseCategory>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseCategory> expenseCategories) {
                expenseCategoryList = expenseCategories;
                populateGraph(expenseCategoryList, expenseList);

            }
        });

        expenseModel.getAllAsLiveData().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                expenseList = expenses;
                populateGraph(expenseCategoryList, expenses);

            }
        });

    }

    private void populateGraph(List<ExpenseCategory> expenseCategories, List<Expense> expenses) {

        if (expenseCategories != null && expenses != null) {
            for (ExpenseCategory category : expenseCategories) {
                entries.add(new PieEntry(100 / expenseCategories.size(), category.getName()));
            }
        }

        PieDataSet set = new PieDataSet(entries, "Expenses");


        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        set.setColors(colors);

        PieData data = new PieData(set);
        mChart.setData(data);
        mChart.invalidate(); // refresh

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        getActivity(),
                        R.drawable.divider
                )
        );

        recyclerView.addItemDecoration(
                itemDecoration
        );


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.budget_breakdown_expenses, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.expense_fragment_recycler_view);
        layoutInflater = getActivity().getLayoutInflater();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        mChart = (PieChart) rootView.findViewById(R.id.expenses_pie_chart);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(30f);
        mChart.setTransparentCircleRadius(32f);
        mChart.setDrawCenterText(false);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setCenterText("Expenses:");
        mChart.setOnChartValueSelectedListener(this);

        mChart.animateY(500, Easing.EasingOption.EaseInOutQuad);
        return rootView;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        if(expenseList!=null) {
        }
    }

    @Override
    public void onNothingSelected() {

    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.budget_recyclerview_item, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Expense currentItem = expenseList.get(position);

            holder.name.setText(currentItem.getName());

            holder.categoryName.setText(currentItem.getCategoryName());
            holder.value.setText(String.valueOf(currentItem.getValue()));
            holder.currency.setText("PLN");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            holder.date.setText(dateFormat.format(currentItem.getDate()));

        }

        @Override
        public int getItemCount() {
            return (expenseList == null) ? 0 : expenseList.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView name;
            private TextView categoryName;
            private TextView value;
            private TextView currency;
            private TextView date;

            private ViewGroup container;

            public CustomViewHolder(View itemView) {
                super(itemView);
                this.name = (TextView) itemView.findViewById(R.id.budget_item_name_textView);
                this.categoryName = (TextView) itemView.findViewById(R.id.budget_item_category_name_textView);
                this.value = (TextView) itemView.findViewById(R.id.budget_item_value_textView);
                this.currency = (TextView) itemView.findViewById(R.id.budget_item_currency_textView);
                this.date = (TextView) itemView.findViewById(R.id.budget_item_date_textView);
                this.container = (ViewGroup) itemView.findViewById(R.id.budget_recyclerview_item_root_constraint);
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }

    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                expenseModel.deleteExpense(expenseList.get(position));
                expenseList.remove(position);
                adapter.notifyItemRemoved(position);
            }

        };
        return simpleItemTouchCallback;
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
