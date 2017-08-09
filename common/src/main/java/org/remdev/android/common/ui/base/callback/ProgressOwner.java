package org.remdev.android.common.ui.base.callback;

import android.support.annotation.StringRes;

/**
 * Created by Alexandr.Salin on 8/31/16.
 */
public interface ProgressOwner {

    void showProgressDialog(String text);

    void showProgressDialog(@StringRes int text);

    void showProgress();

    void hideProgress();

}
