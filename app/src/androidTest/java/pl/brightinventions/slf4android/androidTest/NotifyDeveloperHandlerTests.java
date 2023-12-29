package pl.brightinventions.slf4android.androidTest;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Application;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import pl.brightinventions.slf4android.LoggerConfiguration;
import pl.brightinventions.slf4android.NotifyDeveloperHandler;

public class NotifyDeveloperHandlerTests extends ActivityTestRule<TestActivity> {

    private Logger LOG;
    private NotifyDeveloperHandler handler;

    public NotifyDeveloperHandlerTests() {
        super(TestActivity.class);
    }

    @Before
    public void setUp() {
        LoggerConfiguration.resetConfigurationToDefault();
        LoggerConfiguration configuration = LoggerConfiguration.configuration();
        Application targetContext = (Application) getInstrumentation().getTargetContext().getApplicationContext();
        handler = configuration.notifyDeveloperHandler(targetContext, "piotr.mionskowski@gmail.com");
        configuration.addHandlerToLogger("", handler);
        LOG = LoggerFactory.getLogger(getClass().getSimpleName());
    }

    @Test
    public void test_dont_send_message_with_level_lower_than_error() {
        getActivity();
        LOG.warn("Hello");
    }

    @Test
    public void test_send_message_with_level_error() throws Exception {
        getActivity();
        LOG.warn("Hello");
        LOG.error("Send email", new NullPointerException("A test message"));
        Thread.sleep(TimeUnit.SECONDS.toMillis(15));
    }

    @Test
    public void test_send_message_with_custom_subject_body_with_level_error() throws Exception {
        handler.withSubject("Błąd").withBody("Podaj szczegóły błędu: ");
        getActivity();
        LOG.warn("Hello");
        LOG.error("Send email", new NullPointerException("A test message"));
        Thread.sleep(TimeUnit.SECONDS.toMillis(15));
    }
}
