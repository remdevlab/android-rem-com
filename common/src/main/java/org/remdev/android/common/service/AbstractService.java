package org.remdev.android.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;

/**
 * Created by satalin on 8/9/17.
 */

public abstract class AbstractService extends Service {

    private static final Log logger = LogFactory.create(AbstractService.class);

    protected Messenger mMessenger;

    protected HandlerThread mIncomingThread;

    protected Handler mIncomingHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        final Object lock = new Object();
        mIncomingThread = new HandlerThread("IncomingHandler") {
            protected void onLooperPrepared() {
                mIncomingHandler = new Handler(getLooper()) {

                    @Override
                    public void handleMessage(Message msg) {
                        onHandleMessage(msg);
                    }

                };
                mMessenger = new Messenger(mIncomingHandler);
                lockNotify(lock);
            }
        };
        mIncomingThread.start();
        lockWait(lock);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIncomingThread.quit();
    }

    protected abstract void onHandleMessage(Message msg);

    protected void lockWait(Object lock) {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                logger.e("Interrupted during lockWait", e);
            }
        }
    }

    protected void lockNotify(Object lock) {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}
