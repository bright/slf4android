package pl.brightinventions.slf4android;

import java.util.Date;
import java.util.logging.Level;

public class LogRecord extends java.util.logging.LogRecord {
    private static ThreadLocal<String> currentThreadName = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return Thread.currentThread().getName();
        }
    };

    private final String threadName;
    private final LogLevel logLevel;
    private final Date date;

    /**
     * Constructs a {@code LogRecord} object using the supplied the logging
     * level and message. The millis property is set to the current time. The
     * sequence property is set to a new unique value, allocated in increasing
     * order within the VM. The thread ID is set to a unique value
     * for the current thread. All other properties are set to {@code null}.
     *
     * @param level the logging level, may not be {@code null}.
     * @param msg   the raw message.
     * @throws NullPointerException if {@code level} is {@code null}.
     */
    public LogRecord(Level level, String msg) {
        super(level, msg);
        this.threadName = currentThreadName.get();
        this.logLevel = LogLevel.valueOf(getLevel());
        this.date = new Date(getMillis());
    }

    public static LogRecord fromRecord(java.util.logging.LogRecord r) {
        if (r instanceof LogRecord) {
            return (LogRecord) r;
        }
        LogRecord wrapped = new LogRecord(r.getLevel(), r.getMessage());
        wrapped.setParameters(r.getParameters());
        wrapped.setLoggerName(r.getLoggerName());
        wrapped.setThreadID(r.getThreadID());
        return wrapped;
    }

    public String getThreadName() {
        return threadName;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public Date getDate() {
        return date;
    }
}
