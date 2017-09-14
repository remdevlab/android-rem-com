package org.remdev.android.kotlinapp.activity.presenters

import android.support.v4.app.FragmentActivity
import org.remdev.android.kotlinapp.StatusCodes
import org.remdev.android.kotlinapp.activity.BaseKotlinAppActivity
import org.remdev.android.kotlinapp.commanders.WeatherCommander
import org.remdev.android.kotlinapp.models.WeatherItem
import org.remdev.android.kotlinapp.rest.*

class WeatherPresenter (
        private val activity : FragmentActivity,
        private val callback : WeatherPresenterCallback
) : WeatherDriver.WeatherDriverCallback {

    private val commander : WeatherCommander

    init {
        commander = WeatherCommander(activity, this@WeatherPresenter)
        commander.connectService()
    }

    fun loadWeather(city : String) {
        if (activity is BaseKotlinAppActivity) {
            activity.showProgress("Loading $city weather")
        }
        commander.loadWeather(city)
    }

    override fun onWeatherLoaded(success: Boolean, errorCode: Int, itemList: MutableList<WeatherItem>?) {
        if (activity is BaseKotlinAppActivity) {
            activity.hideProgress()
        }

        if (success) {
            callback.onLoaded(itemList!!)
        } else {
            val msg = when(errorCode) {
                StatusCodes.ERROR_CODE_CITY_NOT_FOUND -> "City was not found"
                StatusCodes.ERROR_CODE_CONNECTION_BROKEN -> "No Internet connection"
                else -> "Unknown error"
            }
            activity.toast(msg)
        }
    }

    fun destroy() {
        commander.disconnect()
    }

    interface WeatherPresenterCallback {
        fun onLoaded(data: List<WeatherItem>);
    }
}