package org.remdev.android.kotlinapp.rest;

import org.remdev.android.commander.models.InteractionResult;
import org.remdev.android.kotlinapp.models.WeatherData;
import org.remdev.android.kotlinapp.models.WeatherItem;

import java.util.List;

public interface WeatherDriver {

    InteractionResult<WeatherData> loadWeather(String city);

    interface WeatherDriverCallback {
        void onWeatherLoaded(boolean success, int errorCode, List<WeatherItem> itemList);
    }
}
