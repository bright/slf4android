package pl.brightinventions.slf4android;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import java.util.logging.Level;

public class MessageValueSupplierTests {
    @Test
    public void canProvideValueOfSimpleString() {
        String message = value("simple message");

        assertThat(message, is(equalTo("simple message")));
    }

    String value(String message, Object... args) {
        MessageValueSupplier messageValueSupplier = new MessageValueSupplier();
        StringBuilder builder = new StringBuilder();
        LogRecord record = new LogRecord(Level.FINE, message);
        record.setParameters(args);
        messageValueSupplier.append(record, builder);
        return builder.toString();
    }

    @Test
    public void canProvideValueOfSimpleMessageWithOneArgument() {
        assertThat(value("message {}", "arg"), is(equalTo("message arg")));
    }

    @Test
    public void canProvideValueOfSimpleMessageWithTwoArguments() {
        assertThat(value("message {} {}", "arg1", "arg2"), is(equalTo("message arg1 arg2")));
    }

    @Test
    public void canProvideValueOfSimpleMessageWithManyArguments() {
        assertThat(value("message {} and {} and {}", "arg1", "arg2", "arg3"), is(equalTo("message arg1 and arg2 and arg3")));
    }

    @Test
    public void canAppendExceptionInformationAtTheEndOfMessage() {
        assertThat(value("message", new NullPointerException()), containsString("message"));
        assertThat(value("message", new NullPointerException()), containsString("NullPointerException"));
    }

    @Test
    public void canProvideFormattedValueWithExceptionInformationAtTheEndOfMessage() {
        assertThat(value("message {}", "arg", new NullPointerException()), containsString("message arg"));
        assertThat(value("message {}", new NullPointerException()), containsString("NullPointerException"));
    }
}
