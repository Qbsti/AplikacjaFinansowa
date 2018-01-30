package com.example.qbook.financeapp.DependencyInjection;

import android.app.Application;

import com.example.qbook.financeapp.FinanceAppApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final FinanceAppApplication application;
    public ApplicationModule(FinanceAppApplication application) {
        this.application = application;
    }

    @Provides
    FinanceAppApplication provideFinanceAppApplication(){
        return application;
    }

    @Provides
    Application provideApplication(){
        return application;
    }
}