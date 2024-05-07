package pl.brightinventions.slf4android;

import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerConfiguration implements LoggerPatternConfiguration {
    private static boolean initialized = false;
    private static LoggerConfiguration configuration;
    private final ArrayList<LoggerPattern> loggerPatterns = new ArrayList<>();
    private final ArrayList<Closeable> thingsToCloseOnReset = new ArrayList<>();
    private final HandlerFormatterCompiler compiler;

    private final org.slf4j.Logger logger;

    LoggerConfiguration() {
        compiler = new HandlerFormatterCompiler(this);
        logger = LoggerFactory.getLogger(getClass().getSimpleName());
    }

    public static LoggerConfiguration resetConfigurationToDefault() {
        if (configuration != null) {
            configuration.close();
        }
        configureDefaults();
        return configuration;
    }

    /**
     * Registers a {@link Closeable} that will be closed upon
     * {@link #resetConfigurationToDefault() configuration reset}.
     */
    public synchronized void registerCloseable(Closeable closeable) {
        thingsToCloseOnReset.add(closeable);
    }

    private synchronized void close() {
        for (Closeable closeable : thingsToCloseOnReset) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.error("Could not close {}", closeable, e);
            }
        }
    }

    private static void configureDefaults() {
        defaultConfiguration();
        setupDefaultRootLoggerHandler();
        configuration.setRootLogLevel(LogLevel.TRACE);
    }

    private static void defaultConfiguration() {
        configuration = new LoggerConfiguration();
        configuration.registerPattern("%newline", new ConstLoggerValueSupplier(System.getProperty("line.separator") != null ? System.getProperty("line.separator") : "\n"));
        configuration.registerPattern("%message", new MessageValueSupplier());
        configuration.registerPattern("%thread", new ThreadValueSupplier());
        configuration.registerPattern("%name", new LoggerNameValueSupplier());
        configuration.registerPattern("%level", new LevelValueSupplier());
        configuration.registerPattern("%date", new DateValueSupplier());
    }

    private static void setupDefaultRootLoggerHandler() {
        Logger rootLogger = removeRootLogHandlers();
        rootLogger.addHandler(new LogcatHandler(configuration.compiler.compile("%message")));
    }

    public LoggerConfiguration setRootLogLevel(LogLevel level) {
        return setLogLevel("", level);
    }

    public void registerPattern(String pattern, LoggerPatternValueSupplier valueSupplier) {
        loggerPatterns.add(0, new LoggerPattern(pattern, valueSupplier));
    }

    private static Logger removeRootLogHandlers() {
        return removeLogHandlers("");
    }

    /**
     * Set log level for given logger name
     */
    public LoggerConfiguration setLogLevel(String loggerName, LogLevel level) {
        Logger logger = LogManager.getLogManager().getLogger(loggerName);
        logger.setLevel(level.getUtilLogLevel());
        return this;
    }

    private static Logger removeLogHandlers(String loggerName) {
        Logger rootLogger = LogManager.getLogManager().getLogger(loggerName);
        for (Handler handler : Arrays.asList(rootLogger.getHandlers())) {
            rootLogger.removeHandler(handler);
        }
        return rootLogger;
    }

    public static LoggerConfiguration configuration() {
        ensureInitialized();
        return configuration;
    }

    static synchronized void ensureInitialized() {
        if (!initialized) {
            initialized = true;
            configureDefaults();
        }
    }

    @Override
    public Iterable<LoggerPattern> getPatterns() {
        return loggerPatterns;
    }

    /**
     * Removes default {@link pl.brightinventions.slf4android.LogcatHandler} from root logger.
     */
    public LoggerConfiguration removeRootLogcatHandler() {
        return removeRootLogHandler(LogcatHandler.class);
    }

    private LoggerConfiguration removeRootLogHandler(Class<LogcatHandler> handlerToRemoveClass) {
        return removeHandlerFromLogger("", handlerToRemoveClass);
    }

    /**
     * Removes all handlers that derive from {@code handlerToRemoveClass} from logger named {@code logger}.
     */
    public LoggerConfiguration removeHandlerFromLogger(String loggerName, Class<? extends Handler> handlerToRemoveClass) {
        Logger rootLogger = LogManager.getLogManager().getLogger(loggerName);
        for (Handler handler : Arrays.asList(rootLogger.getHandlers())) {
            if (handlerToRemoveClass.isAssignableFrom(handler.getClass())) {
                rootLogger.removeHandler(handler);
            }
        }
        return this;
    }

    /**
     * Adds given {@code handler} to logger named {@code loggerName}.
     *
     * @return A {@link Logger} named {@code loggerName}.
     */
    public Logger addHandlerToLogger(String loggerName, Handler handler) {
        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(handler);
        return logger;
    }

    /**
     * Adds given {@code handler} to root logger.
     *
     * @return The root {@link Logger}.
     */
    public Logger addHandlerToRootLogger(Handler handler) {
        Logger logger = Logger.getLogger("");
        logger.addHandler(handler);
        return logger;
    }

    /**
     * Use to compile patterns
     */
    public HandlerFormatterCompiler getCompiler(){
        return this.compiler;
    }
}
