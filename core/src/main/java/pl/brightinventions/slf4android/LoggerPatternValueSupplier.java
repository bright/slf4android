package pl.brightinventions.slf4android;


public interface LoggerPatternValueSupplier {
    void append(LogRecord record, StringBuilder builder);
}
