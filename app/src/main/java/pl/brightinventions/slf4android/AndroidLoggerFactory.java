package pl.brightinventions.slf4android;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.logging.Level;

public class AndroidLoggerFactory implements ILoggerFactory {

    private final HashMap<String, AndroidLoggerAdapter> loggerAdaptersMap = new HashMap<String, AndroidLoggerAdapter>();

    @Override
    public Logger getLogger(String name) {
        AndroidLoggerAdapter adapter = loggerAdaptersMap.get(name);
        if (adapter == null) {
            synchronized (loggerAdaptersMap) {
                adapter = loggerAdaptersMap.get(name);
                if (adapter == null) {
                    adapter = createLogger(name);
                    loggerAdaptersMap.put(name, adapter);
                }
            }
        }
        return adapter;
    }

    private AndroidLoggerAdapter createLogger(String name) {
        LoggerConfiguration.ensureInitialized();
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
        logger.setLevel(Level.ALL);
        return new AndroidLoggerAdapter(logger);
    }

}
