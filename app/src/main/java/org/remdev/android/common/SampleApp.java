package org.remdev.android.common;

import org.remdev.android.common.utils.LocaleUtils;
import org.remdev.android.common.utils.PrefsUtil;
import org.remdev.android.common.utils.TTSController;

import java.util.Locale;

/**
 * Created by satalin on 7/6/17.
 */

public class SampleApp extends LoggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        PrefsUtil.getInstance().putString("key", "value");
        PrefsUtil.getInstance().flushPrefsToLogs();


        Locale locale = LocaleUtils.getLocaleCodeString(this);
        if (locale == null) {
            locale = Locale.US;
        }
        TTSController.init(this, locale);
    }
}
