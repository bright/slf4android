package pl.brightinventions.slf4android;

class LevelValueSupplier implements LoggerPatternValueSupplier {
    @Override
    public void append(LogRecord record, StringBuilder builder) {
        builder.append(record.getLogLevel());
    }
}
