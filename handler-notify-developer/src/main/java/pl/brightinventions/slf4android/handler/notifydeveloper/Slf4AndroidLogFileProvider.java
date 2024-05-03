package pl.brightinventions.slf4android.handler.notifydeveloper;

import android.content.Context;

import androidx.core.content.FileProvider;

public class Slf4AndroidLogFileProvider extends FileProvider {

    public static String getAuthority(Context context) {
        // The suffix must be exactly the same as the one provided in the AndroidManifest.xml
        return context.getPackageName() + ".slf4android.logs.provider";
    }
}
