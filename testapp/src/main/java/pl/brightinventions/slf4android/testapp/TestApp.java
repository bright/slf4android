package pl.brightinventions.slf4android.testapp;

import android.app.Application;

import pl.brightinventions.slf4android.LoggerConfiguration;

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LoggerConfiguration.resetConfigurationToDefault();
    }
}
