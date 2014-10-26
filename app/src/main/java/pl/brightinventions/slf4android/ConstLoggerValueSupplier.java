package pl.brightinventions.slf4android;

import java.util.logging.LogRecord;

class ConstLoggerValueSupplier implements LoggerPatternValueSupplier {
    private final String value;

    public ConstLoggerValueSupplier(String value) {
        this.value = value;
    }

    @Override
    public void append(LogRecord record, StringBuilder builder) {
        builder.append(value);
    }
}
