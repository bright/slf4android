package pl.brightinventions.slf4android.androidTest;

import android.app.Activity;

public class TestActivity extends Activity {
    static Runnable action;

    @Override
    protected void onResume() {
        action.run();
        super.onResume();
    }
}
