package pl.brightinventions.slf4android.testapp;

import android.app.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.brightinventions.slf4android.LoggerConfiguration;

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LoggerConfiguration.resetConfigurationToDefault();
        Logger LOG = LoggerFactory.getLogger("TestApp");

        LOG.debug("debug");
        LOG.info("info");
        LOG.warn("warn");
        LOG.error("error");
        LOG.trace("trace");
    }
}
