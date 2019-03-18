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

    protected static final String DEFAULT_LOG_FILE_NAME = "test-logs.log";
    protected static final int DEFAULT_FILE_SIZE_MB = 2;
    protected static final int DEFAULT_HISTORY_LENGTH = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        LogToFileTree logToFileTree = new LogToFileTree.Builder()
                .logFileName(getLogFileName())
                .logsDir(getLogsDir())
                .fileSizeMB(getFileSizeMB())
                .historyLength(getHistoryLength())
                .build();
        LogFactory.configure(new Timber.DebugTree(), logToFileTree);
        LogFactory.create(this.getClass()).w("start app!");
    }

    protected int getFileSizeMB() {
        return DEFAULT_FILE_SIZE_MB;
    }

    protected int getHistoryLength() {
        return DEFAULT_HISTORY_LENGTH;
    }

    @NonNull
    protected String getLogsDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logs";
    }

    @NonNull
    protected String getLogFileName() {
        return DEFAULT_LOG_FILE_NAME;
    }

}
