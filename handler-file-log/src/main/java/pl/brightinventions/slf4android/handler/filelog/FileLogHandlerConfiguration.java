package pl.brightinventions.slf4android.handler.filelog;

import android.content.Context;

import java.util.logging.Handler;

import pl.brightinventions.slf4android.LogRecordFormatter;
import pl.brightinventions.slf4android.LoggerConfiguration;

public abstract class FileLogHandlerConfiguration extends Handler {
    @SuppressWarnings("UnusedDeclaration")
    public abstract FileLogHandlerConfiguration setFullFilePathPattern(String fullPathPattern);

    @SuppressWarnings("UnusedDeclaration")
    public abstract FileLogHandlerConfiguration setRotateFilesCountLimit(int count);

    @SuppressWarnings("UnusedDeclaration")
    public abstract FileLogHandlerConfiguration setLogFileSizeLimitInBytes(int maxFileSizeInBytes);

    public abstract String getCurrentFileName();


    /**
     * Creates a {@link FileLogHandlerConfiguration} logger handler.
     * The returned new instance should be added to some logger with
     * {@link pl.brightinventions.slf4android.LoggerConfiguration#addHandlerToLogger(String, Handler)}
     *
     * @return A new instance of {@link FileLogHandlerConfiguration}.
     */
    public static FileLogHandlerConfiguration create(Context context) {
        LogRecordFormatter formatter = LoggerConfiguration.configuration()
                .getCompiler()
                .compile("%date %level [%thread] %name - %message%newline");
        return new FileLogHandler(context, formatter);
    }
}
