package pl.brightinventions.slf4android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NotifyDeveloperDialogDisplayActivity extends Activity {
    private static final String TAG = NotifyDeveloperDialogDisplayActivity.class.getSimpleName();
    private static final MessageValueSupplier messageFormatter = new MessageValueSupplier();
    private AlertDialog dialog;

    public static Intent showIntent(Context context, LogRecord record, List<String> emailAddresses, Iterable<String> attachmentsClasses) {
        Intent intent = new Intent(context, NotifyDeveloperDialogDisplayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("log_record", getMessage(record));
        intent.putExtra("email_addresses", new ArrayList<String>(emailAddresses));
        ArrayList<String> attachmentClassNames = new ArrayList<String>();
        for (String reporterClass : attachmentsClasses) {
            attachmentClassNames.add(reporterClass);
        }
        intent.putExtra("attachments", attachmentClassNames);
        return intent;
    }

    private static String getMessage(LogRecord record) {
        StringBuilder builderBuilder = new StringBuilder();
        messageFormatter.append(record, builderBuilder);
        return builderBuilder.toString();
    }

    public static AlertDialog showDialogIn(final Context activityContext, LogRecord record, final List<String> emailAddresses, Iterable<String> attachmentTasks) {
        return showDialogIn(activityContext, getMessage(record), emailAddresses, buildAttachmentFactories(attachmentTasks), null);
    }

    private static AlertDialog showDialogIn(final Context activityContext, final String message, final List<String> emailAddresses, Iterable<AsyncTask<Context, Void, File>> attachmentTasks, final Disposable onDialogClose) {
        final EmailErrorReport emailErrorReport = new EmailErrorReport(message, emailAddresses);
        for (AsyncTask<Context, Void, File> attachment : attachmentTasks) {
            if (Build.VERSION.SDK_INT < 11) {
                attachment.execute(activityContext);
            } else {
                attachment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, activityContext);
            }
            emailErrorReport.addFileAttachmentFrom(attachment);
        }
        String shortMessage = message;
        int end = message.indexOf("\n");
        if (end > 0) {
            shortMessage = message.substring(0, end);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(activityContext)
                .setTitle("Notify developer about error?")
                .setMessage(shortMessage)
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmailWithError(activityContext, emailErrorReport);
                        dialog.dismiss();
                        if (onDialogClose != null) {
                            onDialogClose.dispose();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (onDialogClose != null) {
                            onDialogClose.dispose();
                        }
                    }
                })
                .create();
        alertDialog.show();
        return alertDialog;
    }

    private static List<AsyncTask<Context, Void, File>> buildAttachmentFactories(Iterable<String> attachmentsClasses) {
        List<AsyncTask<Context, Void, File>> attachmentTasks = new ArrayList<AsyncTask<Context, Void, File>>();
        for (String attachmentClassName : attachmentsClasses) {
            Class<? extends AsyncTask<Context, Void, File>> attachmentClass = loadAttachmentFactoryClass(attachmentClassName);
            if (attachmentClass == null) {
                continue;
            }
            try {
                attachmentTasks.add(attachmentClass.newInstance());
            } catch (InstantiationException e) {
                Log.e(TAG, "Can't create attachment factory from class " + attachmentClassName + " " + e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Can't create attachment factory from class " + attachmentClassName + " " + e);
            }
        }
        return attachmentTasks;
    }

    private static void sendEmailWithError(Context activityContext, EmailErrorReport emailErrorReport) {
        Intent sendEmail = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendEmail.setType("message/rfc822");

        emailErrorReport.configureRecipients(sendEmail);
        emailErrorReport.configureSubject(sendEmail, activityContext);
        emailErrorReport.configureMessage(sendEmail);
        emailErrorReport.configureAttachments(sendEmail);

        try {
            activityContext.startActivity(Intent.createChooser(sendEmail, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activityContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends AsyncTask<Context, Void, File>> loadAttachmentFactoryClass(String attachmentClassName) {
        Class<? extends AsyncTask<Context, Void, File>> attachmentClass = null;
        try {
            attachmentClass = (Class<? extends AsyncTask<Context, Void, File>>) Class.forName(attachmentClassName);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Class not found for attachment " + attachmentClassName + " " + e);
        }
        return attachmentClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String log_record = (String) getIntent().getSerializableExtra("log_record");
        List<String> emailAddresses = (List<String>) getIntent().getSerializableExtra("email_addresses");
        List<AsyncTask<Context, Void, File>> attachmentTasks = buildAttachmentFactoriesFromIntent();
        if (log_record != null) {
            dialog = showDialogIn(this, log_record, emailAddresses, attachmentTasks, new Disposable() {
                @Override
                public void dispose() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    private List<AsyncTask<Context, Void, File>> buildAttachmentFactoriesFromIntent() {
        List<String> attachmentsClasses = getAttachmentFactoriesClassNames();
        return buildAttachmentFactories(attachmentsClasses);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> getAttachmentFactoriesClassNames() {
        return (ArrayList<String>) getIntent().getSerializableExtra("attachments");
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }
}
