package pl.brightinventions.slf4android.androidTest;

import android.test.AndroidTestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.brightinventions.slf4android.LogLevel;
import pl.brightinventions.slf4android.LoggerConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class LoggerAdapterTests extends AndroidTestCase {

    public static final String LOGGER_NAME = "ApplicationTest";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        LoggerConfiguration.resetConfigurationToDefault();
        clearLogcat();
    }

    private void clearLogcat() {
        try {
            Runtime.getRuntime().exec("logcat -c").waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void test_debug_message() throws Exception {
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

    public void test_debug_message_multi_arg() throws Exception {
        getLogger().debug("Message {} {} {} {}", 1, 2, 3, 4);
        assertThat(getLastMessage(), containsString("Message 1 2 3 4"));
    }

    public void test_info_message() throws Exception {
        getLogger().info("With string '{}'", "string arg");
        assertThat(getLastMessage(), containsString("With string 'string arg'"));
    }

    public void test_warning_message() throws Exception {
        getLogger().warn("warning message", new NullPointerException("Bad"));
        String lastMessage = getLastMessage();
        assertThat(lastMessage, containsString("warning message"));
        assertThat(lastMessage, containsString(getClass().getName()));
    }

    public void test_error_message() throws Exception {
        getLogger().error("error message", new IllegalArgumentException("Wrong argument"));
        String lastMessage = getLastMessage();

        assertThat(lastMessage, containsString("error message"));
        assertThat(lastMessage, containsString("IllegalArgumentException"));
    }

    public void test_info_message_when_level_is_set_to_warning() {
        LoggerConfiguration.resetConfigurationToDefault();
        LoggerConfiguration configuration = LoggerConfiguration.configuration();
        configuration.setRootLogLevel(LogLevel.WARNING);
        getLogger().info("info message with exception", new NullPointerException("Bad"));
        String lastMessage = getLastMessage();
        assertThat(lastMessage, not(containsString("info message with exception")));

    }
}