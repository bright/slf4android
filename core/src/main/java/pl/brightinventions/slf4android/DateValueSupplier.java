package pl.brightinventions.slf4android;

import java.text.SimpleDateFormat;
import java.util.Locale;

class DateValueSupplier implements LoggerPatternValueSupplier {
    private static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.US);
        }
    };

    @Override
    public void append(LogRecord record, StringBuilder builder) {
        String formatted = dateFormat.get().format(record.getDate());
        builder.append(formatted);
    }
}
