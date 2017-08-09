package org.remdev.android.common.service;

/**
 * Created by satalin on 8/10/17.
 */
public interface IGenericListener<T> {

    void onSuccess(T result);

    void onFail(Throwable throwable);

    void onCancel();
}
