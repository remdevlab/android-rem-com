package org.remdev.android.common.utils;

import android.content.Context;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by satalin on 7/27/17.
 */

public class DialogHelper {
    public static MaterialDialog buildProgressDialog(Context context, @StringRes int title, @StringRes int content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    public static MaterialDialog buildProgressDialog(Context context, @StringRes int title, String content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }
}
