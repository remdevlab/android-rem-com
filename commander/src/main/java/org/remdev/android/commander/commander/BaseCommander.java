package org.remdev.android.commander.commander;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;

import org.remdev.android.commander.ResultCodes;
import org.remdev.android.commander.models.MessageBundle;
import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public abstract class BaseCommander<T> {

    private static final Log log = LogFactory.create(BaseCommander.class);

    private final Context context;
    private Messenger messenger;
    private ServiceConnection serviceConnection;
    private T callback;
    private CommandConnectionListener commandConnectionListener;
    private ResultReceiver resultReceiver;
    private Queue<Message> messageQueue;
    private boolean active = false;

    protected BaseCommander(Context context, @NonNull final T callback, CommandConnectionListener commandConnectionListener) {
        this.context = context;
        this.callback = callback;
        this.commandConnectionListener = commandConnectionListener;

        messageQueue = new LinkedBlockingQueue<>();
        serviceConnection = createServiceConnection();
        resultReceiver = createResultReceiver();
    }

    protected abstract boolean dispatchResult(boolean success, int command, Bundle resultData, T callback);

    @NonNull
    protected abstract Class<? extends Service> getServiceClass();

    @NonNull
    private ServiceConnection createServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                messenger = new Messenger(service);
                if (commandConnectionListener != null) {
                    commandConnectionListener.onCommanderConnected();
                }
                while (messageQueue.isEmpty() == false) {
                    Message message = messageQueue.poll();
                    if (message != null) {
                        sendMessage(message);
                    }
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                messenger = null;
                if (commandConnectionListener != null) {
                    commandConnectionListener.onCommanderDisconnected();
                }
            }
        };
    }

    public void connectService() {
        context.bindService(new Intent(context, getServiceClass()), serviceConnection, Context.BIND_AUTO_CREATE);
        active = true;
    }


    public void disconnect() {
        log.d("Disconnecting from service");
        context.unbindService(serviceConnection);
        messageQueue.clear();
        active = false;
    }

    protected boolean sendMessage(Message message) {
        if (message == null || active == false) {
            return false;
        }
        if (messenger == null) {
            log.w("Attempt to send message when service is disconnected.");
            messageQueue.add(message);
            return false;
        }
        try {
            messenger.send(message);
            return true;
        } catch (RemoteException e) {
            log.e("Remote exception on attempt to send message");
            return false;
        }
    }

    protected ResultReceiver getResultReceiver() {
        return resultReceiver;
    }

    @NonNull
    private ResultReceiver createResultReceiver() {
        return new ResultReceiver(new Handler(Looper.getMainLooper())) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                boolean success = resultCode == ResultCodes.CODE_OK;
                int command = MessageBundle.extractCommand(resultData);
                boolean processed = dispatchResult(success, command, resultData, callback);
                if (processed == false) {
                    log.d("Can not find result processor for %s command", command);
                }
            }
        };
    }

    public interface CommandConnectionListener {

        void onCommanderConnected();

        void onCommanderDisconnected();
    }
}
