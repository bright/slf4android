package pl.brightinventions.slf4android;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.logging.Level;

class AndroidLoggerAdapter implements Logger {
    private static final Level TRACE_LEVEL = LogLevel.TRACE.getUtilLogLevel();
    private static final Level DEBUG_LEVEL = LogLevel.DEBUG.getUtilLogLevel();
    private static final Level INFO_LEVEL = LogLevel.INFO.getUtilLogLevel();
    private static final Level WARN_LEVEL = LogLevel.WARNING.getUtilLogLevel();
    private static final Level ERROR_LEVEL = LogLevel.ERROR.getUtilLogLevel();
    private final java.util.logging.Logger logger;

    public AndroidLoggerAdapter(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return this.logger.getName();
    }

    @Override
    public void trace(String msg) {
        print(TRACE_LEVEL, msg);
    }

    private void print(Level level, String msg) {
        logger.log(level, msg);
    }

    @Override
    public void trace(String format, Object arg) {
        print(TRACE_LEVEL, format, arg);
    }

    private void print(Level level, String format, Object arg) {
        logger.log(level, format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        print(TRACE_LEVEL, format, arg1, arg2);
    }

    private void print(Level level, String format, Object arg1, Object arg2) {
        logger.log(level, format, new Object[]{arg1, arg2});
    }

    @Override
    public void trace(String format, Object... arguments) {
        print(TRACE_LEVEL, format, arguments);
    }

    private void print(Level level, String format, Object[] args) {
        logger.log(level, format, args);
    }

    @Override
    public void trace(String msg, Throwable t) {
        print(TRACE_LEVEL, msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return isEnabled(TRACE_LEVEL);
    }

    private boolean isEnabled(Level level) {
        return logger.isLoggable(level);
    }

    @Override
    public void trace(Marker marker, String msg) {
        print(TRACE_LEVEL, marker, msg);
    }

    private void print(Level level, Marker marker, String msg) {
        print(level, msg, marker);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        print(TRACE_LEVEL, marker, format, arg);
    }

    private void print(Level level, Marker marker, String format, Object arg) {
        print(level, format, marker, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        print(TRACE_LEVEL, marker, format, arg1, arg2);
    }

    private void print(Level level, Marker marker, String format, Object arg1, Object arg2) {
        print(level, format, new Object[]{marker, arg1, arg2});
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        print(TRACE_LEVEL, marker, format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        print(TRACE_LEVEL, marker, msg, t);
    }

    private void print(Level level, Marker marker, String msg, Throwable t) {
        print(level, msg, new Object[]{marker, t});
    }

    @Override
    public void debug(String msg) {
        print(DEBUG_LEVEL, msg);
    }

    @Override
    public void debug(String format, Object arg) {
        print(DEBUG_LEVEL, format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        print(DEBUG_LEVEL, format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        print(DEBUG_LEVEL, format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        print(DEBUG_LEVEL, msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return isEnabled(DEBUG_LEVEL);
    }

    @Override
    public void debug(Marker marker, String msg) {
        print(DEBUG_LEVEL, marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        print(DEBUG_LEVEL, marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        print(DEBUG_LEVEL, marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        print(DEBUG_LEVEL, marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        print(DEBUG_LEVEL, marker, msg, t);
    }

    @Override
    public void info(String msg) {
        print(INFO_LEVEL, msg);
    }

    @Override
    public void info(String format, Object arg) {
        print(INFO_LEVEL, format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        print(INFO_LEVEL, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        print(INFO_LEVEL, format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        print(INFO_LEVEL, msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return isEnabled(INFO_LEVEL);
    }

    @Override
    public void info(Marker marker, String msg) {
        print(INFO_LEVEL, marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        print(INFO_LEVEL, marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        print(INFO_LEVEL, marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        print(INFO_LEVEL, marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        print(INFO_LEVEL, marker, msg, t);
    }

    @Override
    public void warn(String msg) {
        print(WARN_LEVEL, msg);
    }

    @Override
    public void warn(String format, Object arg) {
        print(WARN_LEVEL, format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        print(WARN_LEVEL, format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        print(WARN_LEVEL, format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        print(WARN_LEVEL, msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return isEnabled(WARN_LEVEL);
    }

    @Override
    public void warn(Marker marker, String msg) {
        print(WARN_LEVEL, marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        print(WARN_LEVEL, marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        print(WARN_LEVEL, marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        print(WARN_LEVEL, marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        print(WARN_LEVEL, marker, msg, t);
    }

    @Override
    public void error(String msg) {
        print(ERROR_LEVEL, msg);
    }

    @Override
    public void error(String format, Object arg) {
        print(ERROR_LEVEL, format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        print(ERROR_LEVEL, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        print(ERROR_LEVEL, format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        print(ERROR_LEVEL, msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return isEnabled(ERROR_LEVEL);
    }

    @Override
    public void error(Marker marker, String msg) {
        print(ERROR_LEVEL, marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        print(ERROR_LEVEL, marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        print(ERROR_LEVEL, marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        print(ERROR_LEVEL, marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        print(ERROR_LEVEL, marker, msg, t);
    }

}
