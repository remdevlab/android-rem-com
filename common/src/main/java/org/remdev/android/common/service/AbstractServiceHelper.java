package org.remdev.android.common.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by satalin on 8/9/17.
 */

public abstract class AbstractServiceHelper {
    private static final Log logger = LogFactory.create(AbstractServiceHelper.class);

    public static final String EXTRA_REQUEST_ID = "request_id";

    public static final String EXTRA_RESULT_CODE = "result_code";

    public static final String EXTRA_RESULT = "result";

    public static final String EXTRA_ERROR = "error";

    public static final int RESULT_CODE_OK = 0;

    public static final int RESULT_CODE_FAIL = 1;

    public static final int RESULT_CODE_CANCEL = 2;

    // One helper instance associated with one context
    protected Context mContext;

    protected Messenger mOutcomingMessenger;

    protected Map<Long, IGenericListener<? extends Object>> mListeners = new HashMap<>();

    private List<PendingRequest> mPendingRequests = new ArrayList<>();

    private BindingState mBindingState = BindingState.UNBOUND;

    protected ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mBindingState = BindingState.BOUND;
            mOutcomingMessenger = new Messenger(service);
            processPendingRequests();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBindingState = BindingState.UNBOUND;
            mOutcomingMessenger = null;
            logger.e("Service disconnected");
        }

    };

    protected AbstractServiceHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    protected abstract Class<?> getAction();

    protected abstract Messenger getIncomingMessenger();

    /**
     * Sends Message from client to proper Service
     *
     * @param msgCode  code of the message to be executed by service
     * @param listener listener for result from executing a message by service
     */
    protected synchronized void sendMessage(IGenericListener<? extends Object> listener, int msgCode,
                                            Bundle requestData) {
        if ((mBindingState == BindingState.BOUND) && (mOutcomingMessenger != null)) {
            long id = System.currentTimeMillis();
            mListeners.put(id, listener);
            Message msg = Message.obtain(null, msgCode);
            msg.replyTo = getIncomingMessenger();
            if (requestData == null) {
                requestData = new Bundle();
            }
            requestData.putLong(EXTRA_REQUEST_ID, id);
            msg.setData(requestData);
            try {
                mOutcomingMessenger.send(msg);
            } catch (RemoteException e) {
                listener.onFail(e);
                mListeners.remove(id);
            }
        } else {
            bindAndAddPendingRequest(msgCode, listener, requestData);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void receiveResult(Message msg, Object result) {
        Bundle data = msg.getData();
        long id = data.getLong(EXTRA_REQUEST_ID);
        IGenericListener listener = mListeners.remove(id);
        if (listener != null) {
            int resultCode = data.getInt(EXTRA_RESULT_CODE);
            switch (resultCode) {
                case RESULT_CODE_OK:
                    listener.onSuccess(result);
                    break;
                case RESULT_CODE_FAIL:
                    listener.onFail((Throwable) data.getSerializable(EXTRA_ERROR));
                    break;
                case RESULT_CODE_CANCEL:
                    listener.onCancel();
                    break;
            }
        }
        if (mListeners.isEmpty() && mBindingState == BindingState.BOUND) {
            mBindingState = BindingState.UNBOUND;
            mContext.unbindService(mConnection);
        }
    }

    private void processPendingRequests() {
        while (!mPendingRequests.isEmpty()) {
            PendingRequest request = mPendingRequests.remove(0);
            sendMessage(request.mListener, request.mMsgCode, request.mRequestData);
        }
    }

    private void bindAndAddPendingRequest(int msgCode, IGenericListener<? extends Object> listener,
                                          Bundle requestData) {
        mPendingRequests.add(new PendingRequest(msgCode, listener, requestData));
        if (mBindingState == BindingState.UNBOUND) {
            mBindingState = BindingState.BINDING;
            mContext.bindService(new Intent(mContext, getAction()), mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private static class PendingRequest {

        private int mMsgCode;

        private IGenericListener<? extends Object> mListener;

        private Bundle mRequestData;

        public PendingRequest(int msgCode, IGenericListener<? extends Object> listener, Bundle requestData) {
            mMsgCode = msgCode;
            mListener = listener;
            mRequestData = requestData;
        }

    }

    private static enum BindingState {
        UNBOUND,
        BINDING,
        BOUND
    }

}
