package pl.brightinventions.slf4android;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.logging.LogRecord;

public class MessageValueSupplier implements LoggerPatternValueSupplier {
    @Override
    public void append(LogRecord record, StringBuilder builder) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(record.getMessage(), record.getParameters());
        String message = formattingTuple.getMessage();
        builder.append(message);
    }
}
