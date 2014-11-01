package pl.brightinventions.slf4android;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class FileLogHandler extends FileLogHandlerConfiguration {
    private static String TAG = FileLogHandler.class.getSimpleName();
    private FileHandler fileHandler;
    private FileHandlerConfigParams config;
    private FileHandlerExpose fileHandlerExpose;

    public FileLogHandler(Context context, LogRecordFormatter formatter) {
        this.config = FileHandlerConfigParams.defaults(context);
        this.config.formatter = formatter;
        this.fileHandlerExpose = new FileHandlerExpose();
    }

    @Override
    public Filter getFilter() {
        ensureInitialized();
        return fileHandler.getFilter();
    }

    private void ensureInitialized() {
        if (fileHandler == null) {
            synchronized (this) {
                if (fileHandler == null) {
                    try {
                        fileHandler = new FileHandler(config.fileName, config.limit, config.count, config.append);
                        fileHandler.setFormatter(config.getFormatterAdapter());
                    } catch (IOException e) {
                        Log.e(TAG, "Could not create FileHandler", e);
                    }
                }
            }
        }
    }

    @Override
    public Formatter getFormatter() {
        ensureInitialized();
        return fileHandler.getFormatter();
    }

    @Override
    public void close() {
        ensureInitialized();
        fileHandler.close();
    }

    @Override
    public void flush() {
        ensureInitialized();
        fileHandler.flush();
    }

    @Override
    public void publish(LogRecord record) {
        ensureInitialized();
        fileHandler.publish(record);
    }

    @Override
    public String getCurrentFileName() {
        ensureInitialized();
        return fileHandlerExpose.getCurrentFileName(fileHandler);
    }

    private static class FileHandlerConfigParams {
        String fileName;
        int limit;
        int count;
        boolean append;
        LogRecordFormatter formatter;

        static FileHandlerConfigParams defaults(Context context) {
            FileHandlerConfigParams configParams = new FileHandlerConfigParams();
            configParams.limit = 1024 * 512; //512KB
            configParams.count = 5;
            configParams.fileName = new File(context.getApplicationInfo().dataDir, context.getPackageName() + ".%g.%u.log").getAbsolutePath();
            configParams.append = true;
            return configParams;
        }

        LogRecordFormatterUtilFormatterAdapter getFormatterAdapter() {
            return new LogRecordFormatterUtilFormatterAdapter(formatter);
        }
    }
}
