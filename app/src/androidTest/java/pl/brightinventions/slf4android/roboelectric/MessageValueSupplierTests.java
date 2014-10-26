package pl.brightinventions.slf4android.roboelectric;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import pl.brightinventions.slf4android.MessageValueSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MessageValueSupplierTests extends RoboelectricTest {
    @Test
    public void canProvideValueOfSimpleString() throws Exception {
        String message = value("simple message");

        assertThat(message, is(equalTo("simple message")));
    }

    @Test
    public void canProvideValueOfSimpleMessageWithOneArgument() throws Exception {
        assertThat(value("message {}", "arg"), is(equalTo("message arg")));
    }

    @Test
    public void canProvideValueOfSimpleMessageWithTwoArguments() throws Exception {
        assertThat(value("message {} {}", "arg1 arg2"), is(equalTo("message arg1 arg2")));
    }

    String value(String message, Object... args) {
        MessageValueSupplier messageValueSupplier = new MessageValueSupplier();
        StringBuilder builder = new StringBuilder();
        LogRecord record = new LogRecord(Level.FINE, message);
        record.setParameters(args);
        messageValueSupplier.append(record, builder);
        return builder.toString();
    }
}
