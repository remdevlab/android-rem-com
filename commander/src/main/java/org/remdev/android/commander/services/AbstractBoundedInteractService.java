package org.remdev.android.commander.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.remdev.android.commander.services.handlers.AbstractCommandsHandler;
import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;


public abstract class AbstractBoundedInteractService extends Service {

    private static final Log log = LogFactory.create(AbstractBoundedInteractService.class);

    private Messenger messenger;
    private AbstractCommandsHandler commandHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        commandHandler = createMessageHandler();
        messenger = new Messenger(commandHandler);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log.d("Binding to service...");
        return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        log.d("Unbinding from service");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (commandHandler != null) {
            commandHandler.destroy();
        }
    }

    @NonNull
    protected abstract AbstractCommandsHandler createMessageHandler();

    @NonNull
    public static Message messageForCommand(int command, ResultReceiver resultReceiver) {
        Message message = Message.obtain();
        message.what = command;
        message.obj = resultReceiver;
        return message;
    }
}
