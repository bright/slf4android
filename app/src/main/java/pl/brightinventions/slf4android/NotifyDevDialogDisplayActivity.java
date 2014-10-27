package pl.brightinventions.slf4android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class NotifyDevDialogDisplayActivity extends Activity {
    private static final MessageValueSupplier messageFormatter = new MessageValueSupplier();
    private AlertDialog dialog;

    public static Intent showIntent(Context context, LogRecord record, List<String> emailAddresses) {
        Intent intent = new Intent(context, NotifyDevDialogDisplayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("log_record", getMessage(record));
        intent.putExtra("email_addresses", new ArrayList<String>(emailAddresses));
        return intent;
    }

    private static String getMessage(LogRecord record) {
        StringBuilder builderBuilder = new StringBuilder();
        messageFormatter.append(record, builderBuilder);
        return builderBuilder.toString();
    }

    public static AlertDialog showDialogIn(final Context activityContext, LogRecord record, final List<String> emailAddresses) {
        return showDialogIn(activityContext, getMessage(record), emailAddresses, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String log_record = (String) getIntent().getSerializableExtra("log_record");
        List<String> emailAddresses = (List<String>) getIntent().getSerializableExtra("email_addresses");
        if (log_record != null) {
            dialog = showDialogIn(this, log_record, emailAddresses, new Disposable() {
                @Override
                public void dispose() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    private static AlertDialog showDialogIn(final Context activityContext, final String message, final List<String> emailAddresses, final Disposable onDialogClose) {
        AlertDialog alertDialog = new AlertDialog.Builder(activityContext)
                .setTitle("Notify developer about error?")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmailWithError(activityContext, message, emailAddresses);
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

    private static void sendEmailWithError(Context activityContext, String message, List<String> emailAddresses) {
        Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.setType("message/rfc822");
        String[] emails = new String[emailAddresses.size()];
        emailAddresses.toArray(emails);
        sendEmail.putExtra(Intent.EXTRA_EMAIL, emails);
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Error in " + activityContext.getPackageName());
        sendEmail.putExtra(Intent.EXTRA_TEXT, "Please provide a description of an error: " + message);
        try {
            activityContext.startActivity(Intent.createChooser(sendEmail, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activityContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
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
