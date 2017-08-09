package org.remdev.android.common.ui.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import org.remdev.android.common.ui.base.callback.ProgressOwner;
import org.remdev.android.common.utils.DialogHelper;
import org.remdev.android.commonlib.R;
import org.remdev.android.commonlib.R2;
import org.remdev.timlog.LogFactory;

import butterknife.BindView;

/**
 * Created by Alexandr.Salin on 7/23/15.
 */
public abstract class BaseActivity extends ButterKnifeActivity implements ProgressOwner {
    private MaterialDialog progressDialog;

    @Nullable
    @BindView(R2.id.progress)
    protected ProgressBar progress;

    @Override
    protected void onStart() {
        super.onStart();
        LogFactory.create(this.getClass()).w("start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogFactory.create(this.getClass()).w("stop");
    }

    protected void beforeInject(Bundle savedInstanceState) {
        super.beforeInject(savedInstanceState);
        LogFactory.create(this.getClass()).w("before inject");
    }

    @Override
    protected void afterInject(Bundle savedInstanceState) {
        super.afterInject(savedInstanceState);
        LogFactory.create(this.getClass()).w("after inject");
    }

    @Override
    public void showProgressDialog(@StringRes int text) {
        progressDialog = DialogHelper.buildProgressDialog(this, R.string.dialog_please_wait, text);
    }

    public void showProgressDialog(String content) {
        progressDialog = DialogHelper.buildProgressDialog(this, R.string.dialog_please_wait, content);
    }

    @Override
    public void showProgress() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }
}