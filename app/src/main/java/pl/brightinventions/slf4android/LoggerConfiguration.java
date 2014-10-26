package pl.brightinventions.slf4android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerConfiguration implements LoggerPatternConfiguration {
    private static boolean initialized = false;
    private static LoggerConfiguration configuration;
    private final ArrayList<LoggerPattern> loggerPatterns = new ArrayList<LoggerPattern>();

    public static synchronized LoggerConfiguration configuration() {
        ensureInitialized();
        return configuration;
    }

    static void ensureInitialized() {
        if (!initialized) {
            initialized = true;
            defaultConfiguration();
            defaultRootLoggerHandler();
        }
    }

    private static void defaultConfiguration() {
        configuration = new LoggerConfiguration();
        configuration.registerPattern("%message", new MessageValueSupplier());
    }

    private static void defaultRootLoggerHandler() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        for (Handler handler : Arrays.asList(rootLogger.getHandlers())) {
            rootLogger.removeHandler(handler);
        }
        rootLogger.addHandler(new LogcatHandler());
    }

    public void registerPattern(String pattern, LoggerPatternValueSupplier valueSupplier) {
        loggerPatterns.add(0, new LoggerPattern(pattern, valueSupplier));
    }

    @Override
    public Iterable<LoggerPattern> getPatterns() {
        return loggerPatterns;
    }
}
