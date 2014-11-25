package pl.brightinventions.slf4android.androidTest;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pl.brightinventions.slf4android.test.R.layout.test);
    }
}
