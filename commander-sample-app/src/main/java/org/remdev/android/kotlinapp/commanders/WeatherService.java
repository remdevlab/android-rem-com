package org.remdev.android.kotlinapp.commanders;

import android.os.Bundle;

import org.remdev.android.commander.models.InteractionResult;
import org.remdev.android.commander.models.Provider;
import org.remdev.android.commander.services.BaseCommanderService;
import org.remdev.android.kotlinapp.rest.ToolboxKt;
import org.remdev.android.kotlinapp.rest.WeatherAPI;
import org.remdev.android.kotlinapp.rest.WeatherDriver;
import org.remdev.android.kotlinapp.rest.WeatherDriverImpl;
import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;


public class WeatherService extends BaseCommanderService {

    private static final Log log = LogFactory.create(WeatherService.class);

    private WeatherDriver weatherDriver;

    public WeatherService() {
        super();
        weatherDriver = new WeatherDriverImpl(new Provider<WeatherAPI>() {
            @Override
            public WeatherAPI get() {
                return ToolboxKt.buildRestApi("https://www.metaweather.com", WeatherAPI.class);
            }
        });
    }

    @Override
    protected InteractionResult executeCommand(int command, Bundle data) {
        switch (command) {
            case WeatherCommander.COMMAND_LOAD_WEATHER:
                return weatherDriver.loadWeather(data.getString(WeatherCommander.LOAD_WEATHER_EXTRA_CITY));
            default:
                log.d("Unsupported command %s", command);
                return null;
        }
    }
}
