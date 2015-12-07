package pl.brightinventions.slf4android;

import org.junit.Test;

import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class HandlerFormatterCompilerTests extends RoboelectricTest {

    @Test
    public void canCompileSimpleMessage() throws Exception {
        LogRecordFormatter formatter = compile("%message");
        String message = formatter.format(new LogRecord(Level.FINE, "My message"));
        assertThat(message, is(equalTo("My message")));
    }

    private LogRecordFormatter compile(String formatter) {
        LoggerConfiguration configuration = new LoggerConfiguration();
        configuration.registerPattern("%message", new MessageValueSupplier());
        return new HandlerFormatterCompiler(configuration).compile(formatter);
    }

    @Test
    public void canCompileSimpleMessageAndPreserveWhiteSpace() throws Exception {
        String message = compile(" %message\t").format(new LogRecord(Level.FINE, "[message]"));
        assertThat(message, is(equalTo(" [message]\t")));
    }

    @Test
    public void canCompileSimpleMessageAndPreserveNonPatternKeywords() throws Exception {
        String message = compile("messageStart %message MESSAGE_end").format(new LogRecord(Level.FINE, "messageContent"));
        assertThat(message, is(equalTo("messageStart messageContent MESSAGE_end")));
    }
}
