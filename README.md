slf4android
===========

A simple implementation of [slf4j api](http://www.slf4j.org/) using android `java.util.logging.*`. This means you can **easily hook in any existing** `java.util.logging.Handler` implementations. 

To use this little gem you'll need to add `http://bright.github.io/maven-repo/` to your repositories:
```groovy
repositories {
    maven {
        url "http://bright.github.io/maven-repo/"
    }
}
```
and then declare a dependency inside a module:
```groovy
dependencies {
    compile('pl.brightinventions:slf4android:0.0.11@aar'){
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
```
FileLogHandlerConfiguration fileHandler = LoggerConfiguration.fileLogHandler(this);

fileHandler.setFullFilePathPattern("/sdcard/your.package/my_log.%g.%u.log");

LoggerConfiguration.configuration().addHandlerToRootLogger(fileHandler);
```
