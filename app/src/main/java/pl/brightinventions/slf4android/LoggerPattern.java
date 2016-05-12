package pl.brightinventions.slf4android;


class LoggerPattern implements LoggerPatternValueSupplier {
    private final String pattern;
    private final LoggerPatternValueSupplier realSupplier;

    public LoggerPattern(String pattern, LoggerPatternValueSupplier realSupplier) {
        if(TextUtils.isEmpty(pattern)){
            throw new IllegalArgumentException("patter is required got '" + pattern + "'");
        }
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
