package pl.brightinventions.slf4android.handler.notifydeveloper;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pl.brightinventions.slf4android.handler.notifydeveloper.androidTest.R.layout.test);
    }
}
