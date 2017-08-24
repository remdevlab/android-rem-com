package org.remdev.android.kotlinapp.loaders

import android.content.Context
import android.os.Bundle
import android.support.v4.content.AsyncTaskLoader
import org.remdev.android.kotlinapp.constants.StatusCodes
import org.remdev.android.kotlinapp.models.CityInfo
import org.remdev.android.kotlinapp.models.InteractionResult
import org.remdev.android.kotlinapp.models.WeatherData
import org.remdev.android.kotlinapp.rest.WeatherAPI
import org.remdev.android.kotlinapp.rest.buildRestApi
import java.net.SocketException

class WeatherLoader (
        private val args : Bundle,
        context : Context
) : AsyncTaskLoader<InteractionResult<WeatherData>>(context) {


    override fun onStartLoading() {
        forceLoad()
    }

    override fun loadInBackground(): InteractionResult<WeatherData> {
        val api = buildRestApi("https://www.metaweather.com", WeatherAPI::class.java)
        val city = args[ARG_CITY] as String?
        city?.let {
            return loadForCity(api, it)
        }
        return InteractionResult.error(StatusCodes.ERROR_CODE_BAD_ARGS)
    }

    private fun loadForCity(api: WeatherAPI, city: String): InteractionResult<WeatherData> {
        try {
            val cityDataResp = api.findCity(city).execute()
            val cityData = cityDataResp.body().firstOrNull()
            cityData?.let {
                return loadWeatherItems(api, it)
            } ?: return InteractionResult.error(StatusCodes.ERROR_CODE_CITY_NOT_FOUND)
        } catch (e: SocketException) {
            return InteractionResult.error(StatusCodes.ERROR_CODE_CONNECTION_BROKEN);
        }
    }

    private fun loadWeatherItems(api: WeatherAPI, it: CityInfo): InteractionResult<WeatherData> {
        try {
            val weatherDataResp = api.loadWeather(it.woeId).execute()
            val weatherData = weatherDataResp.body()
                    ?: return InteractionResult.error(StatusCodes.ERROR_CODE_CITY_NOT_FOUND);
            if (weatherData.details == null) {
                return InteractionResult.success(weatherData)
            }
            return InteractionResult.error(StatusCodes.ERROR_CODE_CITY_NOT_FOUND)
        } catch (e: SocketException) {
            return InteractionResult.error(StatusCodes.ERROR_CODE_CONNECTION_BROKEN);
        }
    }

    companion object {
        val ARG_CITY = "${WeatherLoader::class.qualifiedName}.ARG_CITY"
        fun buildsArgs(city : String) : Bundle {
            val bundle = Bundle()
            bundle.putString(ARG_CITY, city)
            return bundle
        }
    }
}