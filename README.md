slf4android
===========

A simple implementation of [slf4j api](http://www.slf4j.org/) using android `java.util.logging.*`. To use this little gem you'll need to add `http://bright.github.io/maven-repo/` to your repositories:

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
    compile('pl.brightinventions:slf4android:0.0.4@aar'){
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

