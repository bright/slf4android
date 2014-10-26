package pl.brightinventions.slf4android;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

public class MessageValueSupplier implements LoggerPatternValueSupplier {

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Override
    public void append(LogRecord record, StringBuilder builder) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(record.getMessage(), record.getParameters());
        String message = formattingTuple.getMessage();
        Throwable throwable = formattingTuple.getThrowable();
        if (throwable != null) {
            StringWriter writer = new StringWriter(100);
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            printWriter.close();
            writer.flush();
            message += " " + writer.toString();
        }
        builder.append(message);
    }
}
