package pl.brightinventions.slf4android.testapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import pl.brightinventions.slf4android.LoggerConfiguration;

public class MainActivity extends Activity {

    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class.getSimpleName());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.log).setOnClickListener((view) -> {
            LOG.trace("trace");
            LOG.debug("debug");
            LOG.info("info");
            LOG.warn("warn");
            LOG.error("error");
        });
        findViewById(R.id.reset).setOnClickListener((view) -> {
            LoggerConfiguration.resetConfigurationToDefault();
        });
        findViewById(R.id.registerCloseable).setOnClickListener((view) -> {
            LoggerConfiguration.configuration().registerCloseable(() -> LOG.info("Closing"));
        });
        findViewById(R.id.registerFailingCloseable).setOnClickListener((view) -> {
            LoggerConfiguration.configuration().registerCloseable(() -> {
                throw new IOException("Test");
            });
        });

    }
}
