package com.example.qbook.financeapp;

import android.app.Application;
import com.example.qbook.financeapp.DependencyInjection.ApplicationComponent;
import com.example.qbook.financeapp.DependencyInjection.ApplicationModule;
import com.example.qbook.financeapp.DependencyInjection.DaggerApplicationComponent;
import com.example.qbook.financeapp.DependencyInjection.RoomModule;

public class FinanceAppApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
