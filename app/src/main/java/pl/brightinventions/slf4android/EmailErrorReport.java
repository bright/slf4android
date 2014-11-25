package pl.brightinventions.slf4android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

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
    private List<AsyncTask<?, ?, File>> attachments = new ArrayList<AsyncTask<?, ?, File>>();

    EmailErrorReport(String message, List<String> emailAddresses) {
        this.message = message;
        this.emailAddresses = emailAddresses;
    }

    public void addFileAttachmentFrom(AsyncTask<?, ?, File> attachment) {
        attachments.add(attachment);
    }

    public void configureRecipients(Intent sendEmail) {
        String[] emails = new String[emailAddresses.size()];
        emailAddresses.toArray(emails);
        sendEmail.putExtra(Intent.EXTRA_EMAIL, emails);
    }

    public void configureSubject(Intent sendEmail, Context subject) {
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Error in: " + subject.getPackageName());
    }

    public void configureMessage(Intent sendEmail) {
        sendEmail.putExtra(Intent.EXTRA_TEXT, "Please provide a description of an error:\n" + message);
    }

    public void configureAttachments(Intent sendEmail) {
        ArrayList<Uri> attachmentsUris = new ArrayList<Uri>();
        for (AsyncTask<?, ?, File> fileAttachmentSource : attachments) {
            Uri uri = buildAttachmentUri(fileAttachmentSource);
            if (uri != null) {
                attachmentsUris.add(uri);
            }
        }
        sendEmail.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentsUris);
    }

    private Uri buildAttachmentUri(AsyncTask<?, ?, File> attachmentTask) {
        try {
            File file = attachmentTask.get(5, TimeUnit.SECONDS);
            if (file != null) {
                return Uri.fromFile(file);
            } else {
                LOG.warn("Attachment task {} returned null", attachmentTask.getClass().getSimpleName());
            }
        } catch (InterruptedException e) {
            LOG.warn("Interrupted while waiting for attachment", e);
        } catch (ExecutionException e) {
            LOG.warn("Error while waiting for attachment", e);
        } catch (TimeoutException e) {
            LOG.warn("Timed out while waiting for attachment", e);
        }
        return null;
    }
}
