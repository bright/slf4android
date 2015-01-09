package pl.brightinventions.slf4android;

import java.util.logging.Handler;

public abstract class FileLogHandlerConfiguration extends Handler {
    @SuppressWarnings("UnusedDeclaration")
    public abstract FileLogHandlerConfiguration setFullFilePathPattern(String fullPathPattern);

    @SuppressWarnings("UnusedDeclaration")
    public abstract FileLogHandlerConfiguration setRotateFilesCountLimit(int count);

    @SuppressWarnings("UnusedDeclaration")
    public abstract FileLogHandlerConfiguration setLogFileSizeLimitInBytes(int maxFileSizeInBytes);
    public abstract String getCurrentFileName();
}
