package pl.brightinventions.slf4android.handler.filelog;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import pl.brightinventions.slf4android.LogRecordFormatter;

class LogRecordFormatterUtilFormatterAdapter extends Formatter {
    private final LogRecordFormatter formatter;

    public LogRecordFormatterUtilFormatterAdapter(LogRecordFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String format(LogRecord r) {
        return formatter.format(pl.brightinventions.slf4android.LogRecord.fromRecord(r));
    }
}
