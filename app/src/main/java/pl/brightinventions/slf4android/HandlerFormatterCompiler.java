package pl.brightinventions.slf4android;

public class HandlerFormatterCompiler {
    private final LoggerPatternConfiguration configuration;

    public HandlerFormatterCompiler(LoggerPatternConfiguration configuration) {
        this.configuration = configuration;
    }

    public LogRecordFormatter compile(String formatterPattern) {
        ListLogRecordFormatter formatter = new ListLogRecordFormatter();
        while (!TextUtils.isEmpty(formatterPattern)) {
            int firstPatternIndex = Integer.MAX_VALUE;
            LoggerPattern firstPattern = null;
            for (LoggerPattern loggerPattern : configuration.getPatterns()) {
                String pattern = loggerPattern.getPattern();
                int patternIndex = formatterPattern.indexOf(pattern);
                if (patternIndex != -1 && patternIndex < firstPatternIndex) {
                    firstPatternIndex = patternIndex;
                    firstPattern = loggerPattern;
                }
            }
            if (firstPattern != null) {
                if (firstPatternIndex > 0) {
                    formatter.add(new ConstLoggerValueSupplier(formatterPattern.substring(0, firstPatternIndex)));
                }
                formatter.add(firstPattern);
                formatterPattern = formatterPattern.substring(firstPatternIndex + firstPattern.getPattern().length());
            } else {
                formatter.add(new ConstLoggerValueSupplier(formatterPattern));
                formatterPattern = null;
            }
        }
        return formatter;
    }
}
