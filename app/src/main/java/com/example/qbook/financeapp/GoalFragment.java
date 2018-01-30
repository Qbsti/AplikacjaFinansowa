package com.example.qbook.financeapp;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qbook.financeapp.Data.Lifecycle.GoalModel;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;

import java.util.List;

import javax.inject.Inject;

import at.grabner.circleprogress.CircleProgressView;


public class GoalFragment extends Fragment implements LifecycleOwner {
    private static final String EXTRA_GOAL_ID = "EXTRA_GOAL_ID";

    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Context mContext;
    private LifecycleRegistry lifecycleRegistry;
    private LinearLayoutManager layoutManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    GoalModel goalModel;

    private List<Goal> listOfGoals;

    public GoalFragment() {
    }

    public static GoalFragment newInstance() {
        return new GoalFragment();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        goalModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GoalModel.class);

        goalModel.getAllAsLiveData().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable List<Goal> goals) {
                setListofGoals(goals);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goal, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.goal_activity_recycler_view);
        layoutInflater = getActivity().getLayoutInflater();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void setListofGoals(List<Goal> listOfGoals) {
        this.listOfGoals = listOfGoals;
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.goal_linear_recyclerview_item, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Goal currentItem = listOfGoals.get(position);

            holder.circleProgressView.setValueAnimated(currentItem.getMoneySavedForGoal() * 100 / currentItem.getValue(), 750);

            holder.name.setText(currentItem.getName());

            holder.value.setText(String.valueOf(currentItem.getValue()));

            holder.divider.setText("/");

            holder.savedForGoal.setText(String.valueOf(currentItem.getMoneySavedForGoal()));

            holder.currencySymbol.setText("PLN");

            holder.addMoneyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddMoneyToGoalActivity.class);
                    intent.putExtra(EXTRA_GOAL_ID, listOfGoals.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    getActivity().recreate();
                }
            });
        }

        @Override
        public int getItemCount() {
            return (listOfGoals == null) ? 0 : listOfGoals.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private CircleProgressView circleProgressView;
            private TextView name;
            private TextView value;
            private TextView divider;
            private TextView savedForGoal;
            private TextView currencySymbol;
            private Button addMoneyButton;

            private ViewGroup container;

            public CustomViewHolder(View itemView) {
                super(itemView);
                this.circleProgressView = (CircleProgressView) itemView.findViewById(R.id.goal_recycle_item_circleView);
                this.name = (TextView) itemView.findViewById(R.id.goal_recycle_item_name_textView);
                this.value = (TextView) itemView.findViewById(R.id.goal_recycle_item_value_textView);
                this.divider = (TextView) itemView.findViewById(R.id.goal_recycle_item_divider_textView);
                this.savedForGoal = (TextView) itemView.findViewById(R.id.goal_recycle_item_amount_saved_textView);
                this.currencySymbol = (TextView) itemView.findViewById(R.id.goal_recycle_item_currency_textView);
                this.addMoneyButton = (Button) itemView.findViewById(R.id.goal_recycle_item_add_money_button);
                this.container = (ViewGroup) itemView.findViewById(R.id.goal_recyclerview_item_root_constraint);

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
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                goalModel.deleteGoal(listOfGoals.get(position));
                listOfGoals.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
        return simpleItemTouchCallback;
    }
}