package com.android.nike;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.android.nike.di.AppComponent;
import com.android.nike.di.AppModule;
import com.android.nike.di.DaggerAppComponent;

import com.android.nike.network.UrbanAPIService;

public class UrbanApplication extends Application {

    private AppComponent appComponent;
    private static Context appContext;

    public UrbanApplication() {
        this.appContext  = this;
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();

    }

    public static Context getAppContext() {

        return appContext;
    }
    public UrbanAPIService getGitHubService(){

        return appComponent.provideUrbanAPIService();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
