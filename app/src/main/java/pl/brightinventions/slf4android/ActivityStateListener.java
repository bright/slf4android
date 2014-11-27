package pl.brightinventions.slf4android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ActivityStateListener implements Application.ActivityLifecycleCallbacks {
    private Activity lastUsedActivity;
    private Activity activeActivity;
    private Handler pauseHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        lastUsedActivity = activity;
        activeActivity = activity;
    }

    public boolean isAppInForeground() {
        return activeActivity != null;
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        pauseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activeActivity == activity) {
                    activeActivity = null;
                }
            }
        }, 50);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (lastUsedActivity == activity) {
            lastUsedActivity = null;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Activity getLastUsedActivity() {
        return lastUsedActivity;
    }
}
