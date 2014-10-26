package pl.brightinventions.slf4android;

import java.util.logging.LogRecord;
import java.util.regex.Pattern;

class LoggerPattern implements LoggerPatternValueSupplier {
    private final String pattern;
    private final LoggerPatternValueSupplier realSupplier;

    public LoggerPattern(String pattern, LoggerPatternValueSupplier realSupplier) {
        this.pattern = pattern;
        this.realSupplier = realSupplier;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public void append(LogRecord record, StringBuilder builder) {
        realSupplier.append(record, builder);
    }
}
