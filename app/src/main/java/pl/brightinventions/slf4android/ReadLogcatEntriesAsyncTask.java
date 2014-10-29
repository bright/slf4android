package pl.brightinventions.slf4android;

import android.content.Context;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

class ReadLogcatEntriesAsyncTask extends AsyncTask<Context, Void, File> {
    private static final Logger LOG = LoggerFactory.getLogger(ReadLogcatEntriesAsyncTask.class.getSimpleName());

    @Override
    protected File doInBackground(Context... params) {
        if (params == null || params.length == 0 || params[0] == null) {
            LOG.warn("Wrong arguments passed to read logcat entries");
            return null;
        }
        Context ctx = params[0];
        try {
            File tempFile = File.createTempFile("logcat", ".log", ctx.getExternalCacheDir());

            String fullPath = tempFile.getAbsolutePath();

            String command = String.format("logcat -d -f %s", fullPath);

            Runtime runtime = Runtime.getRuntime();
            if (runtime != null) {
                try {
                    Process process = runtime.exec(command);
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        LOG.warn("Command {} returned with code {}", command, exitCode);
                    } else {
                        LOG.info("Dumped logcat entries to {} with size {} KB", fullPath, tempFile.length() / 1024);
                    }
                } catch (IOException e) {
                    LOG.warn("Error dumping logcat entries to {}", fullPath, e);
                } catch (InterruptedException e) {
                    LOG.warn("Error dumping logcat entries to {}", fullPath, e);
                }
            }

            return tempFile;
        } catch (IOException e) {
            LOG.warn("Error creating temp file, did you enable write permissions?", e);
            return null;
        }
    }
}
