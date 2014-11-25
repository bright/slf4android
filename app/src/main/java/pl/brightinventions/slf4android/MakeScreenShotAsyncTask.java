package pl.brightinventions.slf4android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

public class MakeScreenShotAsyncTask extends AsyncTask<Context, Void, File> {
    private static final Logger LOG = LoggerFactory.getLogger(MakeScreenShotAsyncTask.class.getSimpleName());

    @Override
    protected File doInBackground(Context... params) {
        Context context = params[0];
        File result = null;
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            final CountDownLatch latch = new CountDownLatch(1);
            final View activityContent = activity.findViewById(android.R.id.content);
            final Bitmap[] bitmaps = new Bitmap[1];
            if (activityContent == null) {
                LOG.warn("Activity content view is null");
                latch.countDown();
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean wasEnabled = activityContent.isDrawingCacheEnabled();
                            if (!wasEnabled) {
                                activityContent.setDrawingCacheEnabled(true);
                            }
                            LOG.debug("Will now make screen shot of {}", activityContent);
                            if (activityContent.getMeasuredWidth() == 0 && activityContent.getMeasuredHeight() == 0) {
                                activityContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                activityContent.layout(0, 0, activityContent.getMeasuredWidth(), activityContent.getMeasuredHeight());
                            }
                            activityContent.buildDrawingCache();
                            Bitmap drawingCache = activityContent.getDrawingCache();
                            boolean gotBitmap = drawingCache != null;

                            LOG.debug("Got drawing cache from root view {}", gotBitmap);
                            if (gotBitmap) {
                                bitmaps[0] = Bitmap.createBitmap(drawingCache);
                                drawingCache.recycle();
                            }
                            if (!wasEnabled) {
                                activityContent.setDrawingCacheEnabled(false);
                            }
                            latch.countDown();
                        } catch (Exception e) {
                            LOG.warn("Failed to take screen shot", e);
                        }
                    }
                });
            }
            try {
                latch.await();
                if (bitmaps[0] != null) {
                    Bitmap screenShot = bitmaps[0];
                    File cacheDir = context.getExternalCacheDir();
                    if (cacheDir == null) {
                        cacheDir = context.getCacheDir();
                    }
                    if (cacheDir != null) {
                        File bitmapFile = new File(cacheDir, "slf4android_screen.png");
                        if (bitmapFile.exists()) {
                            bitmapFile.delete();
                        }
                        OutputStream out = new FileOutputStream(bitmapFile);
                        screenShot.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        screenShot.recycle();
                        result = bitmapFile;
                    } else {
                        LOG.warn("Failed to find a directory for screen shot");
                    }
                }
            } catch (InterruptedException e) {
                LOG.warn("Interrupted while waiting for screen shot", e);
            } catch (IOException e) {
                LOG.warn("Failed to create bitmap screen shot file", e);
            }

        }
        return result;
    }
}
