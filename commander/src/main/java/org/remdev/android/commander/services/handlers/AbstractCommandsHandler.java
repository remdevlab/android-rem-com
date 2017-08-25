package org.remdev.android.commander.services.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;

import org.remdev.android.commander.ResultCodes;
import org.remdev.android.commander.models.MessageBundle;
import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class AbstractCommandsHandler extends Handler {

    private static final int POOL_SIZE = 5;
    private static final Log log = LogFactory.create(AbstractCommandsHandler.class);

    private ExecutorService executor;

    public AbstractCommandsHandler() {
        super(Looper.getMainLooper());
        executor = Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Override
    public void handleMessage(Message msg) {
        int id = msg.what;
        dispatchCommand(msg, id);
    }

    protected void dispatchCommand(Message msg, final int deviceCommand) {
        final ResultReceiver resultReceiver = (ResultReceiver) msg.obj;
        final Runnable task = createTask(deviceCommand, resultReceiver, msg.getData());
        if (task == null) {
            log.d("No task for command command %s", deviceCommand);
            return;
        }
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Throwable e) {
                    log.e(e, "Could not execute command %s", deviceCommand);
                    resultReceiver.send(ResultCodes.CODE_ERROR, MessageBundle.build(deviceCommand).toBundle());
                }
            }
        });
    }

    protected abstract Runnable createTask(int deviceCommand, ResultReceiver resultReceiver, Bundle data);

    public void destroy() {
        executor.shutdownNow();
    }
}
