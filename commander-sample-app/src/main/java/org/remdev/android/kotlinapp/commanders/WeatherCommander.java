package org.remdev.android.kotlinapp.commanders;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;

import org.remdev.android.commander.ResultCodes;
import org.remdev.android.commander.commander.BaseCommander;
import org.remdev.android.commander.models.InteractionResult;
import org.remdev.android.commander.models.MessageBundle;
import org.remdev.android.kotlinapp.models.WeatherData;
import org.remdev.android.kotlinapp.rest.WeatherDriver;

public class WeatherCommander extends BaseCommander<WeatherDriver.WeatherDriverCallback> {

    public static final int COMMAND_LOAD_WEATHER = 1;
    public static final String LOAD_WEATHER_EXTRA_CITY = WeatherCommander.class.getName() + ".LOAD_WEATHER_EXTRA_CITY";

    public WeatherCommander(Context context, @NonNull WeatherDriver.WeatherDriverCallback callback) {
        super(context, callback, null);
    }

    public void loadWeather(String city) {
        Bundle bundle = new Bundle();
        bundle.putString(LOAD_WEATHER_EXTRA_CITY, city);
        sendCommand(COMMAND_LOAD_WEATHER, bundle);
    }

    @Override
    protected boolean dispatchResult(boolean success, int command, Bundle resultData, WeatherDriver.WeatherDriverCallback callback) {
        return processGenericResult(success, resultData, createGeneralInvoker(command, callback));
    }

    private <T> boolean processGenericResult(boolean success, Bundle resultData, CallbackInvoker<T> invoker) {
        MessageBundle<InteractionResult<T>> messageBundle = MessageBundle.fromBundle(resultData);
        InteractionResult<T> result = messageBundle.getPayload();
        int errorCode = result == null ? ResultCodes.ERROR_CODE_UNKNOWN : result.getErrorCode();
        invoker.invoke(success, errorCode, result == null ? null : result.getPayload());
        return true;
    }

    private void sendCommand(int command) {
        sendMessage(WeatherService.messageForCommand(command, getResultReceiver()));
    }

    private void sendCommand(int command, Bundle date) {
        Message message = WeatherService.messageForCommand(command, getResultReceiver());
        message.setData(date);
        sendMessage(message);
    }

    private <T> CallbackInvoker<T> createGeneralInvoker(final int command, final WeatherDriver.WeatherDriverCallback callback) {
        return new CallbackInvoker<T>() {
            @Override
            public boolean invoke(boolean success, int errorCode, T payload) {
                switch (command) {
                    case COMMAND_LOAD_WEATHER:
                        WeatherData weatherData = (WeatherData) payload;
                        callback.onWeatherLoaded(success, errorCode, weatherData == null ? null : weatherData.getItems());
                        break;
                    default:
                        return false;
                }
                return false;
            }
        };
    }

    private interface CallbackInvoker<T> {
        boolean invoke(boolean success, int errorCode, T payload);
    }

    @NonNull
    @Override
    protected Class<? extends Service> getServiceClass() {
        return WeatherService.class;
    }
}
