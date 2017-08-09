package org.remdev.android.common.ui.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Alexandr.Salin on 1/17/16.
 */
public abstract class ButterKnifeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        beforeInject(savedInstanceState);
        ButterKnife.bind(this);
        afterInject(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    protected void beforeInject(Bundle savedInstanceState) {
    }

    protected void afterInject(Bundle savedInstanceState) {
    }

    @LayoutRes
    protected abstract int getResId();
}
