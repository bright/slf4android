package pl.brightinventions.slf4android.androidTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.brightinventions.slf4android.LogLevel;
import pl.brightinventions.slf4android.LoggerConfiguration;

public class LoggerAdapterTests {

    public static final String LOGGER_NAME = "ApplicationTest";

    @Before
    public void setUp() {
        LoggerConfiguration.resetConfigurationToDefault();
        clearLogcat();
    }

    private void clearLogcat() {
        try {
            Runtime.getRuntime().exec("logcat -c").waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_debug_message() {
        getLogger().debug("Hello debug {} {}", 1, 2);
        assertThat(getLastMessage(), containsString("Hello debug 1 2"));
    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME);
    }

    private String getLastMessage() {
        return getLastMessage(LOGGER_NAME);
    }

    private String getLastMessage(String tag) {
        String result = null;
        try {
            Process process = Runtime.getRuntime().exec(String.format("logcat -d %s:*", tag));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            result = log.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Test
    public void test_debug_message_multi_arg() {
        getLogger().debug("Message {} {} {} {}", 1, 2, 3, 4);
        assertThat(getLastMessage(), containsString("Message 1 2 3 4"));
    }

    @Test
    public void test_info_message() {
        getLogger().info("With string '{}'", "string arg");
        assertThat(getLastMessage(), containsString("With string 'string arg'"));
    }

    @Test
    public void test_warning_message() {
        getLogger().warn("warning message", new NullPointerException("Bad"));
        String lastMessage = getLastMessage();
        assertThat(lastMessage, containsString("warning message"));
        assertThat(lastMessage, containsString(getClass().getName()));
    }

    @Test
    public void test_error_message() {
        getLogger().error("error message", new IllegalArgumentException("Wrong argument"));
        String lastMessage = getLastMessage();

        assertThat(lastMessage, containsString("error message"));
        assertThat(lastMessage, containsString("IllegalArgumentException"));
    }

    @Test
    public void test_info_message_not_printed_when_level_is_set_to_warning() {
        setLevelTo(LogLevel.WARNING);
        getLogger().info("info message with exception", new NullPointerException("Bad"));
        assertThat(getLastMessage(), not(containsString("info message with exception")));
    }

    private void setLevelTo(LogLevel level) {
        LoggerConfiguration.resetConfigurationToDefault();
        LoggerConfiguration configuration = LoggerConfiguration.configuration();
        configuration.setRootLogLevel(level);
    }

    @Test
    public void test_debug_message_not_printed_when_level_is_set_to_info() {
        setLevelTo(LogLevel.INFO);
        getLogger().debug("debug message with exception", new NullPointerException("Bad"));
        assertThat(getLastMessage(), not(containsString("debug message with exception")));
    }

    @Test
    public void test_trace_message_not_printed_when_level_is_set_to_info() {
        setLevelTo(LogLevel.INFO);
        getLogger().trace("new trace message with exception", new NullPointerException("Bad"));
        assertThat(getLastMessage(), not(containsString("new trace message with exception")));
    }

    @Test
    public void test_trace_message_not_printed_when_level_is_set_to_debug() {
        setLevelTo(LogLevel.DEBUG);
        getLogger().trace("trace message with exception", new NullPointerException("Bad"));
        String lastMessage = getLastMessage();
        assertThat(lastMessage, not(containsString("trace message with exception")));
    }

    @Test
    public void test_error_message_printed_when_level_is_set_to_debug() {
        setLevelTo(LogLevel.DEBUG);
        getLogger().error("error message with exception", new NullPointerException("Bad"));
        assertThat(getLastMessage(), containsString("error message with exception"));
    }

    @Test
    public void test_warning_message_printed_when_level_is_set_to_debug() {
        setLevelTo(LogLevel.DEBUG);
        getLogger().error("new warning message with exception", new NullPointerException("Bad"));
        assertThat(getLastMessage(), containsString("new warning message with exception"));
    }
}