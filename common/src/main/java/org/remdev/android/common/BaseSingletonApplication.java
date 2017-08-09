package org.remdev.android.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by satalin on 7/6/17.
 */

public class BaseSingletonApplication extends Application {
    private static BaseSingletonApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
