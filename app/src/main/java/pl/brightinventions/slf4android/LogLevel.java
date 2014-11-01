package pl.brightinventions.slf4android;

import java.util.logging.Level;

public enum LogLevel {
    TRACE(Level.FINEST),
    DEBUG(Level.FINE),
    INFO(Level.INFO),
    WARNING(Level.WARNING),
    ERROR(Level.SEVERE),;
    public final Level utilLogLevel;

    LogLevel(Level utilLogLevel) {

        this.utilLogLevel = utilLogLevel;
    }

    public static LogLevel valueOf(Level utilLogLevel) {
        for (LogLevel level : values()) {
            if (level.utilLogLevel == utilLogLevel) {
                return level;
            }
        }
        return TRACE;
    }

    public Level getUtilLogLevel() {
        return utilLogLevel;
    }

    public boolean isSmallerOrEqualTo(Level level) {
        return utilLogLevel.intValue() <= level.intValue();
    }
}
