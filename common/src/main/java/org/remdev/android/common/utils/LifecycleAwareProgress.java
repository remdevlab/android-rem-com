package org.remdev.android.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.remdev.android.commonlib.R;


/**
 * Created by Siarhei Naliuka
 * email: snaliuka@exadel.com
 */

public class LifecycleAwareProgress {

    private static final String KEY_PROGRESS_SHOWING = LifecycleAwareProgress.class.getName() + ".KEY_PROGRESS_SHOWING";
    private static final String KEY_PROGRESS_MESSAGE = LifecycleAwareProgress.class.getName() + ".KEY_PROGRESS_MESSAGE";
    private static final String KEY_PROGRESS_TITLE = LifecycleAwareProgress.class.getName() + ".KEY_PROGRESS_TITLE";

    private final Activity context;
    private AlertDialog progressDialog;
    private String title;
    private String message;

    public LifecycleAwareProgress(Activity context, Bundle bundle) {
        this.context = context;
        SimpleLifecycleCallbacks.attach(context, this, bundle);
    }

    private void onSaveInstanceState(@NonNull Bundle bundle) {
        if (bundle == null) {
            return;
        }
        bundle.putBoolean(KEY_PROGRESS_SHOWING, progressDialog != null);
        bundle.putString(KEY_PROGRESS_TITLE, title);
        bundle.putString(KEY_PROGRESS_MESSAGE, message);
    }

    private void onRestoreState(@Nullable Bundle bundle) {
        boolean showing = false;
        if (bundle != null) {
            showing = bundle.getBoolean(KEY_PROGRESS_SHOWING, false);
            title = bundle.getString(KEY_PROGRESS_TITLE);
            message = bundle.getString(KEY_PROGRESS_MESSAGE);
        }
        if (showing) {
            showProgress(title, message);
        }
    }

    @UiThread
    public void showProgress(@StringRes int messageId) {
        showProgress(context.getString(messageId));
    }

    @UiThread
    public void showProgress(String message) {
        showProgress(null, message);
    }

    @UiThread
    public void showProgress(@StringRes int titleId, @StringRes int messageId) {
        showProgress(context.getString(titleId), context.getString(messageId));
    }

    @UiThread
    public void showProgress(String title, String message) {
        showProgress(title, message, R.layout.layout_progress_view_content, R.id.tv_message);
    }

    @UiThread
    public void showProgress(String title,
                             String message, @LayoutRes int messageViewLayoutId,
                             @IdRes int messageTextViewId) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        this.title = title;
        this.message = message;
        View progressView = LayoutInflater.from(context).inflate(messageViewLayoutId, null);
        ((TextView) progressView.findViewById(messageTextViewId)).setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(progressView)
                .setCancelable(false)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        progressDialog = null;
                    }
                });
        if (title != null) {
            builder.setTitle(title);
        }
        progressDialog = builder.show();
    }

    @UiThread
    public void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }

    private static class SimpleLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        private LifecycleAwareProgress progressDialog;
        private Activity attachedActivity;

        private SimpleLifecycleCallbacks(Activity context, LifecycleAwareProgress progressDialog, Bundle bundle) {
            this.progressDialog = progressDialog;
            this.attachedActivity = context;
            progressDialog.onRestoreState(bundle);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            if (activity == attachedActivity) {
                progressDialog.onSaveInstanceState(outState);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activity == attachedActivity) {
                attachedActivity = null;
                activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

        @Override
        public void onActivityStarted(Activity activity) { }

        @Override
        public void onActivityResumed(Activity activity) { }

        @Override
        public void onActivityPaused(Activity activity) { }

        @Override
        public void onActivityStopped(Activity activity) { }

        private static SimpleLifecycleCallbacks attach(Activity context, LifecycleAwareProgress dialog, Bundle bundle) {
            SimpleLifecycleCallbacks callback = new SimpleLifecycleCallbacks(context, dialog, bundle);
            context.getApplication().registerActivityLifecycleCallbacks(callback);
            return callback;
        }
    }
}
