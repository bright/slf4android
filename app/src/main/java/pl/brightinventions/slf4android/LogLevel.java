package pl.brightinventions.slf4android;

import android.util.Log;

import java.util.logging.Level;

public enum LogLevel {
    TRACE(Level.FINEST, Log.VERBOSE),
    DEBUG(Level.FINE, Log.DEBUG),
    INFO(Level.INFO, Log.INFO),
    WARNING(Level.WARNING, Log.WARN),
    ERROR(Level.SEVERE, Log.ERROR);

    private final Level utilLogLevel;
    private final int androidLogLevel;

    LogLevel(Level utilLogLevel, int androidLogLevel) {
        this.utilLogLevel = utilLogLevel;
        this.androidLogLevel = androidLogLevel;
    }

    public static LogLevel valueOf(Level utilLogLevel) {
        for (LogLevel level : values()) {
            if (level.utilLogLevel == utilLogLevel) {
                return level;
            }
        }
        return TRACE;
    }

    public boolean isSmallerOrEqualTo(Level level) {
        return utilLogLevel.intValue() <= level.intValue();
    }

    /**
     * Converts a {@link pl.brightinventions.slf4android.LogLevel} logging level into an Android one.
     *
     * @return The resulting Android logging level.
     */
    public int getAndroidLevel() {
        return androidLogLevel;
    }

    /**
     * Converts a {@link pl.brightinventions.slf4android.LogLevel} logging level into a {@link java.util.logging.Level}.
     *
     * @return The resulting {@link java.util.logging.Level}.
     */
    public Level getUtilLogLevel() {
        return utilLogLevel;
    }
}
