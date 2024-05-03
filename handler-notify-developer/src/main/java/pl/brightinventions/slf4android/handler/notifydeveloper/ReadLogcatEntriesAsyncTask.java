package pl.brightinventions.slf4android.handler.notifydeveloper;

import android.content.Context;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

class ReadLogcatEntriesAsyncTask extends AsyncTask<Context, Void, File> {
    private static final Logger LOG = LoggerFactory.getLogger(ReadLogcatEntriesAsyncTask.class.getSimpleName());
    private static LogcatReadingConfiguration LogcatReadingConfig;

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

            String readLogcatCommand = String.format("logcat -v time -d -f %s", fullPath);

            Runtime runtime = Runtime.getRuntime();
            try {
                Process process = runtime.exec(readLogcatCommand);
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    LOG.warn("Command {} returned with code {}", readLogcatCommand, exitCode);
                } else {
                    LOG.info("Dumped logcat entries to {} with size {} KB", fullPath, tempFile.length() / 1024);
                    if (getConfiguration().shouldClear()) {
                        LOG.info("Will now clear logcat entries");
                        runtime.exec("logcat -c");
                    }
                }
            } catch (IOException | InterruptedException e) {
                LOG.warn("Error dumping logcat entries to {}", fullPath, e);
            }

            return tempFile;
        } catch (IOException e) {
            LOG.warn("Error creating temp file, did you enable write permissions?", e);
            return null;
        }
    }

    public static synchronized LogcatReadingConfiguration getConfiguration() {
        if(LogcatReadingConfig == null){
            LogcatReadingConfig = new LogcatReadingConfiguration();
        }
        return LogcatReadingConfig;
    }
}
