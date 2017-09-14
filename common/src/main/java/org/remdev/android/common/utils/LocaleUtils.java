package org.remdev.android.common.utils;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class LocaleUtils {

    public static Locale getLocaleCodeString(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
