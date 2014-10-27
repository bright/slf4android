package pl.brightinventions.slf4android.androidTest;

import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.brightinventions.slf4android.LoggerConfiguration;
import pl.brightinventions.slf4android.NotifyDevOnErrorHandler;

public class NotifyDevOnErrorTests extends ActivityInstrumentationTestCase2<TestActivity> {

    private Logger LOG;

    public NotifyDevOnErrorTests() {
        super(TestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        LoggerConfiguration.resetConfiguration();
        LoggerConfiguration configuration = LoggerConfiguration.configuration();
        Application targetContext = (Application) getInstrumentation().getTargetContext().getApplicationContext();
        NotifyDevOnErrorHandler handler = configuration.notifyDeveloperWithLogcatDataHandler(targetContext, "piotr.mionskowski@gmail.com");
        configuration.addHandlerToLogger("", handler);
        LOG = LoggerFactory.getLogger(getClass().getSimpleName());
        super.setUp();
    }

    public void test_dont_send_message_with_level_lower_than_error() throws Exception {
        LOG.warn("Hello");
    }

    @Test
    public void test_send_message_with_level_error() throws Exception {
        LOG.warn("Hello");
        LOG.error("Send email", new NullPointerException("A test message"));
        Thread.sleep(10000);
    }
}
