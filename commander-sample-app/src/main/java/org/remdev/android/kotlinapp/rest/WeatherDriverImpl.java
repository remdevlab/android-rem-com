package org.remdev.android.kotlinapp.rest;

import org.remdev.android.commander.BaseRequestExecutor;
import org.remdev.android.commander.models.InteractionResult;
import org.remdev.android.commander.models.Provider;
import org.remdev.android.kotlinapp.StatusCodes;
import org.remdev.android.kotlinapp.models.CityInfo;
import org.remdev.android.kotlinapp.models.WeatherData;
import org.remdev.android.kotlinapp.models.WeatherItem;

import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class WeatherDriverImpl extends BaseRequestExecutor implements WeatherDriver {

    private final Provider<WeatherAPI> apiProvider;

    public WeatherDriverImpl(Provider<WeatherAPI> apiProvider) {
        this.apiProvider = apiProvider;
    }

    @Override
    protected boolean isUnauthorizedResponse(String stringBody) {
        return false;
    }

    @Override
    public InteractionResult<WeatherData> loadWeather(String city) {
        Call<ResponseBody> findCityCall = getWeatherAPI().findCity(city);
        ReadBodyResult<CityInfo[]> findCityResult = parseBody(findCityCall, CityInfo[].class);
        if (hasErrors(findCityResult)) {
            return getErrorResult(findCityResult);
        }
        if (findCityResult.getBody() == null || findCityResult.getBody().length == 0) {
            return InteractionResult.Companion.error(StatusCodes.ERROR_CODE_CITY_NOT_FOUND);
        }

        CityInfo cityInfo = findCityResult.getBody()[0];
        Call<ResponseBody> call = getWeatherAPI().loadWeather(cityInfo.getWoeId());
        ReadBodyResult<WeatherData> result = parseBody(call, WeatherData.class);
        if (hasErrors(result)) {
            return getErrorResult(result);
        }
        if (result.emptyBody()) {
            return InteractionResult.Companion.error(StatusCodes.ERROR_CODE_CITY_NOT_FOUND);
        }
        return InteractionResult.Companion.success(result.getBody());
    }

    private WeatherAPI getWeatherAPI() {
        return apiProvider.get();
    }
}
