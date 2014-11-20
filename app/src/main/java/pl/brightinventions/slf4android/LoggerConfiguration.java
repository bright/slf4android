package pl.brightinventions.slf4android;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerConfiguration implements LoggerPatternConfiguration {
    private static boolean initialized = false;
    private static LoggerConfiguration configuration;
    private final ArrayList<LoggerPattern> loggerPatterns = new ArrayList<LoggerPattern>();
    private final ArrayList<Disposable> disposeThingsOnReset = new ArrayList<Disposable>();
    private final HandlerFormatterCompiler compiler;

    public LoggerConfiguration() {
        compiler = new HandlerFormatterCompiler(this);
    }

    public static LoggerConfiguration resetConfigurationToDefault() {
        if (configuration != null) {
            configuration.dispose();
        }
        configureDefaults();
        return configuration;
    }

    private void dispose() {
        for (Disposable dispose : disposeThingsOnReset) {
            dispose.dispose();
        }
    }

    private static void configureDefaults() {
        defaultConfiguration();
        setupDefaultRootLoggerHandler();
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

    public void registerPattern(String pattern, LoggerPatternValueSupplier valueSupplier) {
        loggerPatterns.add(0, new LoggerPattern(pattern, valueSupplier));
    }

    private static Logger removeRootLogHandlers() {
        return removeLogHandlers("");
    }

    private static Logger removeLogHandlers(String loggerName) {
        Logger rootLogger = LogManager.getLogManager().getLogger(loggerName);
        for (Handler handler : Arrays.asList(rootLogger.getHandlers())) {
            rootLogger.removeHandler(handler);
        }
        return rootLogger;
    }

    /**
     * Creates a {@link pl.brightinventions.slf4android.FileLogHandlerConfiguration} logger handler.
     * The returned new instance should be added to some logger with {@link #addHandlerToLogger(String, java.util.logging.Handler)}
     *
     * @return A new instance of {@link pl.brightinventions.slf4android.FileLogHandlerConfiguration}.
     */
    public static FileLogHandlerConfiguration fileLogHandler(Context context) {
        LogRecordFormatter formatter = configuration().compiler.compile("%date %level [%thread] %name - %message%newline");
        return new FileLogHandler(context, formatter);
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

    public NotifyDeveloperHandler notifyDeveloperHandler(final Application context, String email) {
        final ActivityStateListener stateListener = getStateListener(context);
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(email);
        NotifyDeveloperHandler handler = new NotifyDeveloperHandler(context, emails, stateListener);
        handler.addAttachmentClass(ReadLogcatEntriesAsyncTask.class);
        return handler;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private ActivityStateListener getStateListener(final Application context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            final ActivityStateListener stateListener = new ActivityStateListener();
            disposeThingsOnReset.add(new Disposable() {
                @Override
                public void dispose() {
                    context.unregisterActivityLifecycleCallbacks(stateListener);
                }
            });
            context.registerActivityLifecycleCallbacks(stateListener);
            return stateListener;
        }
        return null;
    }

    /**
     * Adds given {@code handler} to logger named {@code loggerName}.
     *
     * @return A {@link java.util.logging.Logger} named {@code loggerName}.
     */
    public Logger addHandlerToLogger(String loggerName, Handler handler) {
        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(handler);
        return logger;
    }

    /**
     * Adds given {@code handler} to root logger.
     *
     * @return The root {@link java.util.logging.Logger}.
     */
    public Logger addHandlerToRootLogger(Handler handler) {
        Logger logger = Logger.getLogger("");
        logger.addHandler(handler);
        return logger;
    }
}
