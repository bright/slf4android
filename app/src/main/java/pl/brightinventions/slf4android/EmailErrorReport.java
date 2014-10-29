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
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Error in : " + subject.getPackageName());
    }

    public void configureMessage(Intent sendEmail) {
        sendEmail.putExtra(Intent.EXTRA_TEXT, "Please provide a description of an error:\n" + message);
    }

    public void configureAttachments(Intent sendEmail) {
        for (AsyncTask<?, ?, File> fileAttachmentSource : attachments) {
            configureAttachment(sendEmail, fileAttachmentSource);
        }
    }

    private void configureAttachment(Intent sendEmail, AsyncTask<?, ?, File> readLogcatEntries) {
        try {
            File file = readLogcatEntries.get(5, TimeUnit.SECONDS);
            if (file != null) {
                sendEmail.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            } else {
                LOG.warn("Reading logcat entries returned null");
            }
        } catch (InterruptedException e) {
            LOG.warn("Timed out while waiting for logcat entries", e);
        } catch (ExecutionException e) {
            LOG.warn("Error while waiting for logcat entries", e);
        } catch (TimeoutException e) {
            LOG.warn("Timed out while waiting for logcat entries", e);
        }
    }
}
