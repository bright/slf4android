package pl.brightinventions.slf4android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class NotifyDevOnErrorHandler extends Handler {
    private final AtLeastFilter filter;
    private final Context context;
    private final List<String> emailAddress;
    private final int messageCount;
    private final WeakReference<ActivityStateListener> activityState;
    private AlertDialog dialog;
    private android.os.Handler mailLoopHandler = new android.os.Handler(Looper.getMainLooper());

    NotifyDevOnErrorHandler(Application context, Iterable<String> emailAddress, ActivityStateListener activityState) {
        this(context, emailAddress, 10, LogLevel.ERROR, activityState);
    }

    NotifyDevOnErrorHandler(Application context, Iterable<String> emailAddress, int messageCount, LogLevel minLevel, ActivityStateListener stateListener) {
        this.context = context;
        this.emailAddress = Lists.newArrayList(emailAddress);
        this.messageCount = messageCount;
        this.filter = new AtLeastFilter(minLevel);
        this.activityState = new WeakReference<ActivityStateListener>(stateListener);
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
            mailLoopHandler.post(new ShowDialogBecauseOfRecord(record));
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
        private final LogRecord record;

        public ShowDialogBecauseOfRecord(LogRecord record) {
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
                dialog = NotifyDevDialogDisplayActivity.showDialogIn(currentActivity, record, emailAddress);
            } else {
                Intent showDialogActivityIntent = NotifyDevDialogDisplayActivity.showIntent(context, record, emailAddress);
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
