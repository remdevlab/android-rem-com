package org.remdev.android.common;

import org.remdev.android.common.utils.PrefsUtil;

/**
 * Created by satalin on 7/6/17.
 */

public class SampleApp extends LoggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        PrefsUtil.getInstance().putString("key", "value");
        PrefsUtil.getInstance().flushPrefsToLogs();
    }
}
