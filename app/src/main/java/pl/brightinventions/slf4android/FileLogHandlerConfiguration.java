package pl.brightinventions.slf4android;

import java.util.logging.Handler;

public abstract class FileLogHandlerConfiguration extends Handler {
    public abstract String getCurrentFileName();
}
