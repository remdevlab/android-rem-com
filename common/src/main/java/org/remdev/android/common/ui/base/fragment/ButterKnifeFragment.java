package org.remdev.android.common.ui.base.fragment;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alexandr.Salin on 1/13/16.
 */
public abstract class ButterKnifeFragment extends Fragment {
    private View rootView;
    private Unbinder mUnBinder;

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getResourceId(), container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        afterInject();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    protected abstract int getResourceId();

    protected void afterInject() {
    }

    protected View getRootView() {
        return rootView;
    }
}
