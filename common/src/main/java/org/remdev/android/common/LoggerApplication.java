package org.remdev.android.common;

import android.os.Environment;
import android.support.annotation.NonNull;

import org.remdev.timlog.LogFactory;
import org.remdev.timlog.LogToFileTree;

import java.io.File;

import timber.log.Timber;

/**
 * Created by satalin on 7/6/17.
 */

public class LoggerApplication extends BaseSingletonApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LogToFileTree logToFileTree = new LogToFileTree.Builder()
                .logFileName(getLogFileName())
                .logsDir(getLogsDir())
                .fileSizeMB(getFileSizeMB())
                .build();
        LogFactory.configure(new Timber.DebugTree(), logToFileTree);
        LogFactory.create(this.getClass()).w("start app!");
    }

    private int getFileSizeMB() {
        return 2;
    }

    @NonNull
    private String getLogsDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logs";
    }

    @NonNull
    private String getLogFileName() {
        return "test-logs.log";
    }


}
