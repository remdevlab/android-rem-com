package org.remdev.android.common.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import org.remdev.timlog.LogFactory;

/**
 * Created by Alexandr.Salin on 1/17/16.
 */
public class IntentUtil {

    public static void startActivity(Context context,
                                     Class<? extends AppCompatActivity> activityClazzToStart) {
        Intent intent = new Intent(context, activityClazzToStart);
        startActivity(context, intent);
    }

    /**
     * start activity with flags {@link Intent#FLAG_ACTIVITY_NEW_TASK} and {@link Intent#FLAG_ACTIVITY_CLEAR_TOP}
     */
    public static void startActivityNewTask(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(context, intent);
    }

    public static void startActivity(Context context, Intent intent) {
        LogFactory.create(IntentUtil.class).i("Starting {}", intent);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                LogFactory.create(IntentUtil.class).e("Unable to resolve explicit activity intent");
            }
        } else {
            LogFactory.create(IntentUtil.class).e("Unable to resolve activity");
        }
    }

    public static void startActivityForResult(AppCompatActivity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void shareHtmlToMail(Activity activity, String subject, String body, Uri attachmentUri) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachmentUri);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.escapeHtml(body));
        activity.startActivity(Intent.createChooser(emailIntent, "Email:"));
    }
}
