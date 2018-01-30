package com.example.qbook.financeapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import com.example.qbook.financeapp.Data.Lifecycle.IncomeCategoryModel;
import com.example.qbook.financeapp.Data.Lifecycle.IncomeModel;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Income;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.IncomeCategory;
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

import javax.inject.Inject;

public class BudgetBreakdownIncomesFragment extends Fragment implements OnChartValueSelectedListener, LifecycleOwner {

    private PieChart mChart;
    private LifecycleRegistry lifecycleRegistry;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    IncomeModel incomeModel;
    IncomeCategoryModel incomeCategoryModel;

    List<Income> incomeList;
    List<IncomeCategory> incomeCategoryList;
    List<PieEntry> entries = new ArrayList<>();


    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private LinearLayoutManager layoutManager;

    public BudgetBreakdownIncomesFragment() {
    }

    public static BudgetBreakdownIncomesFragment newInstance() {
        return new BudgetBreakdownIncomesFragment();
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

        incomeModel = ViewModelProviders.of(this, viewModelFactory)
                .get(IncomeModel.class);

        incomeCategoryModel = ViewModelProviders.of(this,viewModelFactory)
                .get(IncomeCategoryModel.class);


        incomeModel.getAllAsLiveData().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> incomes) {
incomeList = incomes;
                populateGraph(incomeCategoryList, incomeList);

            }
        });

        incomeCategoryModel.getAllAsLiveData().observe(this, new Observer<List<IncomeCategory>>() {
            @Override
            public void onChanged(@Nullable List<IncomeCategory> incomeCategories) {
                incomeCategoryList=incomeCategories;
                populateGraph(incomeCategoryList, incomeList);

            }
        });

    }
    private void populateGraph(List<IncomeCategory> incomeCategories, List<Income> incomes) {

        if (incomeCategories != null && incomes != null) {
            for (IncomeCategory category : incomeCategories) {
                entries.add(new PieEntry(100 / incomeCategories.size(), category.getName()));
            }
        }

        PieDataSet set = new PieDataSet(entries, "Expenses");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
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
        View rootView = inflater.inflate(R.layout.budget_breakdown_incomes, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.income_fragment_recycler_view);
        layoutInflater = getActivity().getLayoutInflater();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        mChart = (PieChart) rootView.findViewById(R.id.incomes_pie_chart);
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
        mChart.setCenterText("Incomes:");
        mChart.setOnChartValueSelectedListener(this);
        mChart.animateY(500, Easing.EasingOption.EaseInOutQuad);
        return rootView;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
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
            Income currentItem = incomeList.get(position);

            holder.name.setText(currentItem.getName());

            holder.categoryName.setText(currentItem.getCategoryName());
            holder.value.setText(String.valueOf(currentItem.getValue()));
            holder.currency.setText("PLN");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                holder.date.setText(dateFormat.format(currentItem.getDate()));
        }

        @Override
        public int getItemCount() {
            return (incomeList == null) ? 0 : incomeList.size();
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

            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                incomeModel.deleteIncome(incomeList.get(position));
                incomeList.remove(position);
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
