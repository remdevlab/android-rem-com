package org.remdev.android.common;


import android.os.Bundle;

import org.remdev.android.common.ui.base.activity.BaseActivity;
import org.remdev.android.common.utils.LifecycleAwareProgress;

import butterknife.OnClick;

public class ProgressSampleActivity extends BaseActivity {

    protected LifecycleAwareProgress progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new LifecycleAwareProgress(this, savedInstanceState);
    }

    @Override
    protected int getResId() {
        return R.layout.activity_progress_sample;
    }

    @OnClick(R.id.btn_wait)
    protected void калiПачакаць() {
        progress.showProgress("Пакачайте, калi ласка");
    }
}
