slf4android
===========

A simple implementation of [slf4j api](http://www.slf4j.org/) using android `java.util.logging.*`. This means you can **easily hook in any existing** `java.util.logging.Handler` implementations. 

To use this little gem you'll need to add `https://jitpack.io` to your repositories:
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```
and then declare a dependency inside a module:
```groovy
dependencies {
    compile('com.github.bright:slf4android:0.1.3'){
      transitive = true
    }
    //other dependencies
}
```
As with any slf4j compatible implementation using slf4android looks like this:
```java
class HomeActivity extends Activity {
    private static final Logger LOG = LoggerFactory.getLogger(HomeActivity.class.getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("Hello from {} saved instance state {}", this, savedInstanceState);
    }
}
```
### Logging to Crashlytics
[Crashlytics](https://get.fabric.io/crashlytics) has a nice feature of attaching log entries that were issued just before a crash. With slf4android it's easy to add a handler so that whenever a crash happens you get more insight as to what happened just before it.

To achieve that you need a custom handler:
```java
import com.crashlytics.android.Crashlytics;
import java.util.logging.Handler;
import pl.brightinventions.slf4android.LogRecord;
import pl.brightinventions.slf4android.MessageValueSupplier;

public class CrashlyticsLoggerHandler extends Handler {
    MessageValueSupplier messageValueSupplier = new MessageValueSupplier();

    @Override
    public void publish(java.util.logging.LogRecord record) {
        LogRecord logRecord = LogRecord.fromRecord(record);
        StringBuilder messageBuilder = new StringBuilder();
        messageValueSupplier.append(logRecord, messageBuilder);
        String tag = record.getLoggerName();
        int androidLogLevel = logRecord.getLogLevel().getAndroidLevel();
        Crashlytics.log(androidLogLevel, tag, messageBuilder.toString());
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }
}
```

That is added to root logger:
```java
LoggerConfiguration.configuration()
        .removeRootLogcatHandler()
        .addHandlerToRootLogger(new CrashlyticsLoggerHandler());
```
Note that we remove a default logcat handler since Crashlytics will push messages to logcat too.


### Logging to a file
To print messages to a separate file just add:
```java
FileLogHandlerConfiguration fileHandler = LoggerConfiguration.fileLogHandler(this);
LoggerConfiguration.configuration().addHandlerToRootLogger(fileHandler);
String logFileName = fileHandler.getCurrentFileName();
// logFileName contains full path to logged file
```
inside your custom `android.app.Application` `onCreate` method. This will create rotated log files inside `context.getApplicationInfo().dataDir` with a name derived from `context.getPackageName()` and a default message pattern `%date %level [%thread] %name - %message%newline`

To change the location of log file you can use:
```java
FileLogHandlerConfiguration fileHandler = LoggerConfiguration.fileLogHandler(this);

fileHandler.setFullFilePathPattern("/sdcard/your.package/my_log.%g.%u.log");

LoggerConfiguration.configuration().addHandlerToRootLogger(fileHandler);
```
