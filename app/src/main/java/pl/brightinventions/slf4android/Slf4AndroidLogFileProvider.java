package pl.brightinventions.slf4android;

import android.content.Context;
import android.support.v4.content.FileProvider;

public class Slf4AndroidLogFileProvider extends FileProvider {

    public static String getAuthority(Context context) {
        // The suffix must be exactly the same as the one provided in the AndroidManifest.xml
        return context.getPackageName() + ".slf4android.logs.provider";
    }
}
