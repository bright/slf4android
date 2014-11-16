package pl.brightinventions.slf4android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class NotifyDeveloperHandler extends Handler {
    private final Context context;
    private final List<String> emailAddress;
    private final WeakReference<ActivityStateListener> activityState;
    private Filter filter;
    private AlertDialog dialog;
    private android.os.Handler mailLoopHandler = new android.os.Handler(Looper.getMainLooper());

    NotifyDeveloperHandler(Application context, Iterable<String> emailAddress, ActivityStateListener activityState) {
        this(context, emailAddress, LogLevel.ERROR, activityState);
    }

    NotifyDeveloperHandler(Application context, Iterable<String> emailAddress, LogLevel minLevel, ActivityStateListener stateListener) {
        this.context = context;
        this.emailAddress = Lists.newArrayList(emailAddress);
        this.filter = new AtLeastFilter(minLevel);
        this.activityState = new WeakReference<ActivityStateListener>(stateListener);
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
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(LogRecord record) {
        if (filter.isLoggable(record)) {
            mailLoopHandler.post(new ShowDialogBecauseOfRecord(pl.brightinventions.slf4android.LogRecord.fromRecord(record)));
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
                dialog = NotifyDeveloperDialogDisplayActivity.showDialogIn(currentActivity, record, emailAddress);
            } else {
                Intent showDialogActivityIntent = NotifyDeveloperDialogDisplayActivity.showIntent(context, record, emailAddress);
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
}
