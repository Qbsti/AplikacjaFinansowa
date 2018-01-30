package com.example.qbook.financeapp;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qbook.financeapp.Data.Lifecycle.GoalModel;
import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Goal;

import java.util.List;

import javax.inject.Inject;

public class AddGoalActivity extends AppCompatActivity {

    private LifecycleRegistry lifecycleRegistry;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    GoalModel goalModel;


    public AddGoalActivity() {
    }

    public static AddGoalActivity newInstance() {
        return new AddGoalActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        setTitle("Add goal");

        ((FinanceAppApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        goalModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GoalModel.class);


        final Button addGoal = (Button) findViewById(R.id.save_goal_button);
        addGoal.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        saveNewGoal(view);
    }
});
    }

    public void saveNewGoal( View view) {
        final EditText goalName = (EditText) findViewById(R.id.goal_name_input_text);
        final EditText goalValue = (EditText) findViewById(R.id.goal_value_input);

        Goal goal = new Goal();
        goal.setName(goalName.getText().toString());
        goal.setValue(Float.valueOf(goalValue.getText().toString()));
        goal.setMoneySavedForGoal(0);
        goal.setDone(false);

        goalModel.addNewGoal(goal);
        Toast.makeText(AddGoalActivity.this,goal.toString(),Toast.LENGTH_LONG).show();
        finish();
    }
}
