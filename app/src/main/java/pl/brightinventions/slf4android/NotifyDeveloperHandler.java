package pl.brightinventions.slf4android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.squareup.seismic.ShakeDetector;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class NotifyDeveloperHandler extends Handler {
    private static final String TAG = NotifyDeveloperHandler.class.getSimpleName();
    private final Context context;
    private final List<String> emailAddress;
    private final WeakReference<ActivityStateListener> activityState;
    private final ShakeDetector shakeDetector;
    private String emailSubject;
    private String emailBody;
    private Filter filter;
    private AlertDialog dialog;
    private android.os.Handler mailLoopHandler = new android.os.Handler(Looper.getMainLooper());
    private ArrayList<String> attachmentClassList;

    NotifyDeveloperHandler(Application context, Iterable<String> emailAddress, ActivityStateListener activityState) {
        this(context, emailAddress, LogLevel.ERROR, activityState);
    }

    NotifyDeveloperHandler(Application context, Iterable<String> emailAddress, LogLevel minLevel, final ActivityStateListener stateListener) {
        this.context = context;
        this.emailAddress = Lists.newArrayList(emailAddress);
        this.filter = new AtLeastFilter(minLevel);
        this.activityState = new WeakReference<ActivityStateListener>(stateListener);
        this.attachmentClassList = new ArrayList<String>();
        this.shakeDetector = new ShakeDetector(new ShakeDetector.Listener() {
            @Override
            public void hearShake() {
                ActivityStateListener listener = activityState.get();
                if (listener != null) {
                    if (listener.isAppInForeground()) {
                        beginPublishOnMainThread(new LogRecord(Level.INFO, "Report a problem with app"));
                    } else {
                        Log.i(TAG, "Ignore shake event - the app appears to be in background");
                    }
                } else {
                    Log.i(TAG, "Ignore shake event - can't detect if app is in foreground (API < 14)");
                }
            }
        });
        this.emailSubject = context.getString(R.string.email_subject) + context.getPackageName();
        this.emailBody = context.getString(R.string.email_extra_text);
    }

    private void beginPublishOnMainThread(LogRecord record) {
        mailLoopHandler.post(new ShowDialogBecauseOfRecord(pl.brightinventions.slf4android.LogRecord.fromRecord(record)));
    }

    public void setMinLogLevel(LogLevel logLevel) {
        setFilter(new AtLeastFilter(logLevel));
    }

    public void setFilter(Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Filter must not be null");
        }
        this.filter = filter;
    }

    @Override
    public void close() {
        mailLoopHandler.removeCallbacksAndMessages(null);
        if (dialog != null) {
            dialog.dismiss();
        }
        shakeDetector.stop();
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(LogRecord record) {
        if (filter.isLoggable(record)) {
            beginPublishOnMainThread(record);
        }
    }

    private boolean isQuestionPending() {
        return dialog != null && dialog.isShowing();
    }

    private void destroyDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void addAttachmentClass(Class<? extends AsyncTask<Context, Void, File>> attachmentClass) {
        if (attachmentClass == null) {
            throw new IllegalArgumentException("attachmentClass must not be null");
        }
        try {
            attachmentClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Can't create attachment factory from class " + attachmentClass, e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't create attachment factory from class " + attachmentClass, e);
        }
        attachmentClassList.add(attachmentClass.getName());
    }

    public void notifyWhenDeviceIsShaken() {
        if (activityState.get() != null) {
            SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            if (manager == null) {
                Log.w(TAG, "Sensor manager is null will not install shake reporter");
            } else {
                if (!shakeDetector.start(manager)) {
                    Log.w(TAG, "Failed to start shake detector");
                }
            }
        } else {
            Log.w(TAG, "Will not notify on shake - can't detect if app is in foreground (API < 14)");
        }

    }

    private ArrayList<String> getAttachmentClassList() {
        return attachmentClassList;
    }

    private class ShowDialogBecauseOfRecord implements Runnable {
        private final pl.brightinventions.slf4android.LogRecord record;

        public ShowDialogBecauseOfRecord(pl.brightinventions.slf4android.LogRecord record) {
            this.record = record;
        }

        @Override
        public void run() {
            if (isQuestionPending()) {
                return;
            }

            destroyDialog();

            Context currentActivity = obtainActivityContext();
            if (currentActivity != null) {
                dialog = NotifyDeveloperDialogDisplayActivity.showDialogIn(currentActivity,
                        record,
                        emailAddress,
                        emailSubject,
                        emailBody,
                        getAttachmentClassList());
            } else {
                Intent showDialogActivityIntent = NotifyDeveloperDialogDisplayActivity.showIntent(context,
                        record,
                        emailAddress,
                        emailSubject,
                        emailBody,
                        getAttachmentClassList());
                context.startActivity(showDialogActivityIntent);
            }
        }

        private Activity obtainActivityContext() {
            ActivityStateListener activityStateListener = activityState.get();
            Activity activity = null;
            if (activityStateListener != null) {
                activity = activityStateListener.getLastUsedActivity();
            }
            return activity;
        }
    }

    public void withSubject(String subject){
        this.emailSubject = subject;
    }

    public void withBody(String body){
        this.emailBody = body;
    }
}
