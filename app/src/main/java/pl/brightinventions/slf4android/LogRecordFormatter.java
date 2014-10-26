package pl.brightinventions.slf4android;

import java.util.logging.LogRecord;

public interface LogRecordFormatter {
    String format(LogRecord record);
}
