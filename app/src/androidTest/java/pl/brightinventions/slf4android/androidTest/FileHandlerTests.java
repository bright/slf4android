package pl.brightinventions.slf4android.androidTest;

import android.test.AndroidTestCase;

import org.fest.util.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LoggerConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class FileHandlerTests extends AndroidTestCase {
    private Logger LOG;
    private FileLogHandlerConfiguration handler;

    @Override
    protected void setUp() throws Exception {
        LoggerConfiguration.resetConfigurationToDefault();
        handler = LoggerConfiguration.fileLogHandler(getContext());
        LoggerConfiguration.configuration().addHandlerToLogger(getClass().getSimpleName(), handler);
        LOG = LoggerFactory.getLogger(getClass().getSimpleName());

        super.setUp();
    }

    public void test_can_log_debug_into_file() throws Exception {
        LOG.debug("Hello {}", "my friend!");

        String entries = readAndClearFileEntries();

        assertThat(entries, containsString("Hello my friend!"));
    }

    private String readAndClearFileEntries() throws IOException {
        handler.flush();
        String currentFileName = handler.getCurrentFileName();
        String contents = Files.contentOf(new File(currentFileName), Charset.forName("UTF-8"));
        FileChannel outChan = new FileOutputStream(currentFileName, true).getChannel();
        outChan.truncate(0);
        outChan.close();
        return contents;
    }

    public void test_can_log_info_with_level_into_file() throws Exception {
        LOG.info("Hello {}", "my friend!");

        String entries = readAndClearFileEntries();

        assertThat(entries, containsString("INFO"));
    }

    public void test_can_log_warn_with_excpetion_into_file() throws Exception {
        LOG.warn("Hello {}", "my friend!", new RuntimeException("A test"));

        String entries = readAndClearFileEntries();

        assertThat(entries, containsString("RuntimeException"));
    }

    public void test_can_log_logger_name_into_file() throws Exception {
        LOG.error("Hello {}", "my friend!");

        String entries = readAndClearFileEntries();

        assertThat(entries, containsString(LOG.getName()));
    }

    public void test_can_log_thread_name_into_file() throws Exception {
        LOG.error("Hello {}", "my friend!");

        String entries = readAndClearFileEntries();

        assertThat(entries, containsString(Thread.currentThread().getName()));
    }

    public void test_can_log_date_into_file() throws Exception {
        LOG.error("Hello {}", "my friend!");

        String entries = readAndClearFileEntries();

        assertThat(entries, containsString(String.valueOf(new GregorianCalendar().get(Calendar.YEAR))));
    }
}
