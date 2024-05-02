package pl.brightinventions.slf4android.testapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }
}
