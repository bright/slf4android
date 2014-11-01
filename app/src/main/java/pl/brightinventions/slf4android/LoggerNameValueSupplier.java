package pl.brightinventions.slf4android;

class LoggerNameValueSupplier implements LoggerPatternValueSupplier {
    @Override
    public void append(LogRecord record, StringBuilder builder) {
        builder.append(record.getLoggerName());
    }
}
