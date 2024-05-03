package pl.brightinventions.slf4android.handler.notifydeveloper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.core.content.FileProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class EmailErrorReport {
    private static final Logger LOG = LoggerFactory.getLogger(EmailErrorReport.class.getSimpleName());
    private final List<String> emailAddresses;
    private String message;
    private String emailSubject;
    private String emailBody;
    private List<AsyncTask<?, ?, File>> attachments = new ArrayList<AsyncTask<?, ?, File>>();

    EmailErrorReport(String message, List<String> emailAddresses, String emailSubject, String emailBody) {
        this.message = message;
        this.emailAddresses = emailAddresses;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }

    public void addFileAttachmentFrom(AsyncTask<?, ?, File> attachment) {
        attachments.add(attachment);
    }

    public void configureRecipients(Intent sendEmail) {
        String[] emails = new String[emailAddresses.size()];
        emailAddresses.toArray(emails);
        sendEmail.putExtra(Intent.EXTRA_EMAIL, emails);
    }

    public void configureSubject(Intent sendEmail) {
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
    }

    public void configureMessage(Intent sendEmail) {
        sendEmail.putExtra(Intent.EXTRA_TEXT, emailBody + message);
    }

    public void configureAttachments(Intent sendEmail, Context context) {
        String authority = Slf4AndroidLogFileProvider.getAuthority(context);

        ArrayList<Uri> attachmentsUris = new ArrayList<Uri>();
        for (AsyncTask<?, ?, File> fileAttachmentSource : attachments) {
            Uri uri = buildAttachmentUri(fileAttachmentSource, context, authority);
            if (uri != null) {
                attachmentsUris.add(uri);
            }
        }
        sendEmail.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentsUris);
        sendEmail.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    private Uri buildAttachmentUri(AsyncTask<?, ?, File> attachmentTask, Context context, String authority) {
        try {
            File file = attachmentTask.get(5, TimeUnit.SECONDS);
            if (file != null) {
                return FileProvider.getUriForFile(context, authority, file);
            } else {
                LOG.warn("Attachment task {} returned null", attachmentTask.getClass().getSimpleName());
            }
        } catch (InterruptedException e) {
            LOG.warn("Interrupted while waiting for attachment", e);
        } catch (ExecutionException e) {
            LOG.warn("Error while waiting for attachment", e);
        } catch (TimeoutException e) {
            LOG.warn("Timed out while waiting for attachment", e);
        } catch (IllegalArgumentException e) {
            LOG.warn("The selected file can't be shared", e);
        }
        return null;
    }
}
