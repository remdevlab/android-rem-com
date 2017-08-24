package org.remdev.android.kotlinapp.models;

import org.remdev.android.kotlinapp.annotations.POJO;
import org.remdev.android.kotlinapp.constants.StatusCodes;
import org.remdev.android.kotlinapp.rest.ToolboxKt;
import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;

import java.util.HashMap;
import java.util.Map;


public class InteractionResult<T> implements JsonSerializable<InteractionResult<T>> {

    private static final Log log = LogFactory.create(InteractionResult.class);

    private static final String FIELD_CODE = "code";
    private static final String FIELD_ERROR_CODE = "errorCode";
    private static final String FIELD_PAYLOAD_CLASS = "payloadClass";
    private static final String FIELD_PAYLOAD = "payload";

    private int code;
    private int errorCode;
    @POJO
    private T payload;

    public InteractionResult() { }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccessful() {
        return code == StatusCodes.CODE_OK;
    }

    @Override
    public String toJson() {
        Map<String, String> map = new HashMap<>();
        map.put(FIELD_CODE, String.valueOf(code));
        map.put(FIELD_ERROR_CODE, String.valueOf(errorCode));
        if (payload != null) {
            map.put(FIELD_PAYLOAD_CLASS, payload.getClass().getName());
            map.put(FIELD_PAYLOAD, ToolboxKt.toJson(payload));
        }
        return ToolboxKt.toJson(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public InteractionResult<T> fromJson(String json) {
        return parseFromJson(json);
    }

    public <R> InteractionResult<R> changePayload(R newPayload) {
        InteractionResult<R> result = new InteractionResult<>();
        result.setCode(this.getCode());
        result.setErrorCode(this.getErrorCode());
        result.setPayload(newPayload);
        return result;
    }

    public static InteractionResult success() {
        return success(null);
    }

    public static <T> InteractionResult<T> success(@POJO T payload) {
        InteractionResult<T> result = new InteractionResult<>();
        result.code = StatusCodes.CODE_OK;
        result.errorCode = 0;
        result.setPayload(payload);
        return result;
    }

    public static <T> InteractionResult<T> error() {
        return error(0);
    }

    public static <T> InteractionResult<T> error(int errorCode) {
        return error(errorCode, null);
    }

    public static <T> InteractionResult<T> error(@POJO T payload) {
        return error(0, payload);
    }

    public static <T> InteractionResult<T> error(int errorCode, @POJO T payload) {
        InteractionResult<T> result = new InteractionResult<>();
        result.code = StatusCodes.CODE_ERROR;
        result.errorCode = errorCode;
        result.setPayload(payload);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static<T> InteractionResult<T> parseFromJson(String json) {
        if (json == null) {
            return null;
        }
        Map map = ToolboxKt.fromJson(json, Map.class);
        String code = String.valueOf(map.get(FIELD_CODE));
        String errorCode = String.valueOf(map.get(FIELD_ERROR_CODE));
        String className = (String) map.get(FIELD_PAYLOAD_CLASS);
        String payloadJson = (String) map.get(FIELD_PAYLOAD);
        if (code == null || errorCode == null) {
            throw new IllegalArgumentException("Invalid json: " + json);
        }
        int resCode = Integer.parseInt(code);
        int resErrorCode = Integer.parseInt(errorCode);
        T payload = null;
        if (className != null) {
            try {
                Class<T> cls = (Class<T>) Class.forName(className);
                payload = ToolboxKt.fromJson(payloadJson, cls);
            } catch (ClassNotFoundException e) {
                log.e("Could not found class %s", className);
                throw new IllegalArgumentException("Invalid json: "+ json);
            }
        }
        InteractionResult<T> res = new InteractionResult<>();
        res.setCode(resCode);
        res.setErrorCode(resErrorCode);
        res.setPayload(payload);
        return res;
    }
}
