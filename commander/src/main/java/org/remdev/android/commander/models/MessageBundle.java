package org.remdev.android.commander.models;

import android.os.Bundle;

import org.remdev.android.commander.utils.ConversionUtils;

public class MessageBundle<T> {

    private static final String EXTRA_RESULT_DATA = MessageBundle.class.getName() + ".EXTRA_RESULT_DATA";
    private static final String EXTRA_COMMAND_ID = MessageBundle.class.getName() + ".COMMAND_ID";
    private static final String EXTRA_PAYLOAD_CLASS = MessageBundle.class.getName() + ".PAYLOAD_CLASS";

    private int deviceCommand;
    private T payload;

    private MessageBundle(int deviceCommand) {
        this(deviceCommand, null);
    }

    public MessageBundle(int deviceCommand, T payload) {
        this.deviceCommand = deviceCommand;
        this.payload = payload;
    }

    public Bundle toBundle() {
        Bundle result = new Bundle();
        result.putInt(EXTRA_COMMAND_ID, deviceCommand);
        if (payload != null) {
            result.putString(EXTRA_PAYLOAD_CLASS, payload.getClass().getName());
            if (JsonSerializable.class.isAssignableFrom(payload.getClass())) {
                result.putString(EXTRA_RESULT_DATA, ((JsonSerializable) payload).toJson());
            } else {
                result.putString(EXTRA_RESULT_DATA, ConversionUtils.Companion.toJson(payload));
            }
        }
        return result;
    }

    public T getPayload() {
        return payload;
    }

    public static <T> MessageBundle<T> build(int deviceCommand) {
        return build(deviceCommand, null);
    }

    public static <T> MessageBundle<T> build(int deviceCommand, T payload) {
        return new MessageBundle<T>(deviceCommand, payload);
    }

    @SuppressWarnings("unchecked")
    public static <T> MessageBundle<T> fromBundle(Bundle bundle) {
        int command = bundle.getInt(EXTRA_COMMAND_ID);
        String clsName = bundle.getString(EXTRA_PAYLOAD_CLASS);
        String jsonData = bundle.getString(EXTRA_RESULT_DATA);
        T payload = null;
        if (clsName != null) {
            try {
                Class<T> cls = (Class<T>) Class.forName(clsName);
                if (InteractionResult.class.isAssignableFrom(cls)) {
                    payload = (T) InteractionResult.Companion.parseFromJson(jsonData);
                } else {
                    payload = ConversionUtils.Companion.fromJson(jsonData, cls);
                }
            } catch (ClassNotFoundException e) {
                payload = null;
            }
        }
        return new MessageBundle<>(command, payload);
    }

    public static int extractCommand(Bundle bundle) {
        return bundle.getInt(EXTRA_COMMAND_ID);
    }

    public int getCommand() {
        return deviceCommand;
    }
}
