package org.remdev.android.common.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.remdev.timlog.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Alexandr.Salin on 5/4/16.
 */
public class AssetsUtil {
    private final static String TAG = AssetsUtil.class.getSimpleName();

    public static InputStream getAssetInputStream(Context context, String assetFileName) throws IOException {
        return context.getAssets().open(assetFileName);
    }

    public static Reader readFile(Context context, String fileName) throws IOException {
        InputStream inputStream = getAssetInputStream(context, fileName);
        Reader reader = new InputStreamReader(inputStream, "utf-8");
        return reader;
    }

    public static String readFileAsString(Context context, String fileName) {
        try {
            return IOUtil.readFileAsString(context.getAssets().open(fileName));
        } catch (IOException e) {
            LogFactory.create(TAG).e(e.toString(), e);
        }
        return null;
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            LogFactory.create(TAG).e(e.toString(), e);
        }
        return image;
    }
}
