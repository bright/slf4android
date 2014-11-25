package pl.brightinventions.slf4android.androidTest;

import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import pl.brightinventions.slf4android.LoggerConfiguration;
import pl.brightinventions.slf4android.NotifyDeveloperHandler;

public class NotifyDeveloperHandlerTests extends ActivityInstrumentationTestCase2<TestActivity> {

    private Logger LOG;

    public NotifyDeveloperHandlerTests() {
        super(TestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        LoggerConfiguration.resetConfigurationToDefault();
        LoggerConfiguration configuration = LoggerConfiguration.configuration();
        Application targetContext = (Application) getInstrumentation().getTargetContext().getApplicationContext();
        NotifyDeveloperHandler handler = configuration.notifyDeveloperHandler(targetContext, "piotr.mionskowski@gmail.com");
        configuration.addHandlerToLogger("", handler);
        LOG = LoggerFactory.getLogger(getClass().getSimpleName());
        super.setUp();
    }

    public void test_dont_send_message_with_level_lower_than_error() throws Exception {
        getActivity();
        LOG.warn("Hello");
    }

    public void test_send_message_with_level_error() throws Exception {
        getActivity();
        LOG.warn("Hello");
        LOG.error("Send email", new NullPointerException("A test message"));
        Thread.sleep(TimeUnit.SECONDS.toMillis(15));
    }
}
