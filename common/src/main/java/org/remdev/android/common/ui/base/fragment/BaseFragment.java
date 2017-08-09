package org.remdev.android.common.ui.base.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ProgressBar;

import org.remdev.android.common.ui.base.callback.ProgressOwner;
import org.remdev.android.commonlib.R2;
import org.remdev.timlog.LogFactory;

import butterknife.BindView;

/**
 * Created by Alexandr.Salin on 7/23/15.
 */
public abstract class BaseFragment extends ButterKnifeFragment implements ProgressOwner {

    @Nullable
    @BindView(R2.id.progress)
    protected ProgressBar progress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogFactory.create(this.getClass()).w("attach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogFactory.create(this.getClass()).w("detach");
    }

    public void showProgressDialog() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressDialog(String ignore) {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgress() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressDialog(@StringRes int ignore) {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }
}
