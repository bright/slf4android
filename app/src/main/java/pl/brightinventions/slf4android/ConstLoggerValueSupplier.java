package pl.brightinventions.slf4android;

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
