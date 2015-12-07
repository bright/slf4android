package pl.brightinventions.slf4android;

import org.junit.runners.model.InitializationError;

public class RobolectricTestRunner extends org.robolectric.RobolectricTestRunner {
    public RobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
//
//    @Override
//    protected AndroidManifest getAppManifest(Config config) {
//        String manifestPath = "main/AndroidManifest.xml";
//        String resPath = "main/res";
//        AndroidManifest manifest = new AndroidManifest(Fs.fileFromPath(manifestPath), Fs.fileFromPath(resPath)) {
//            @Override
//            public int getTargetSdkVersion() {
//                return 18;
//            }
//        };
//        manifest.setPackageName("pl.brightinventions.slf4android");
//        return manifest;
//    }
//

}
