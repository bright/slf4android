package pl.brightinventions.slf4android;

import java.util.logging.LogRecord;

public interface LoggerPatternValueSupplier {
    void append(LogRecord record, StringBuilder builder);
}
