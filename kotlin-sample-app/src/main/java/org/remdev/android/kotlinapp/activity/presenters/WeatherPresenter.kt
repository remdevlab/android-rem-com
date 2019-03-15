package org.remdev.android.kotlinapp.activity.presenters

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import org.remdev.android.kotlinapp.activity.BaseKotlinAppActivity
import org.remdev.android.kotlinapp.constants.StatusCodes
import org.remdev.android.kotlinapp.loaders.WeatherLoader
import org.remdev.android.kotlinapp.models.InteractionResult
import org.remdev.android.kotlinapp.models.WeatherData
import org.remdev.android.kotlinapp.models.WeatherItem
import org.remdev.android.kotlinapp.rest.toast

class WeatherPresenter (
        private val activity : FragmentActivity,
        private val callback : WeatherPresenterCallback
) : LoaderManager.LoaderCallbacks<InteractionResult<WeatherData>> {

    fun loadWeather(city : String) {
        if (activity is BaseKotlinAppActivity) {
            activity.showProgress("Loading $city weather")
        }
        activity.supportLoaderManager.restartLoader(0, WeatherLoader.buildsArgs(city), this);
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<InteractionResult<WeatherData>> {
        return WeatherLoader(args!!, activity)
    }

    override fun onLoaderReset(loader: Loader<InteractionResult<WeatherData>>) {}

    override fun onLoadFinished(loader: Loader<InteractionResult<WeatherData>>, data: InteractionResult<WeatherData>?) {
        if (activity is BaseKotlinAppActivity) {
            activity.hideProgress()
        }
        if (data == null) {
            return
        }
        if (data.isSuccessful) {
            callback.onLoaded(data.payload.items)
        } else {
            val msg = when(data.errorCode) {
                StatusCodes.ERROR_CODE_BAD_ARGS -> "Invalid arguments!"
                StatusCodes.ERROR_CODE_CITY_NOT_FOUND -> "City was not found"
                StatusCodes.ERROR_CODE_CONNECTION_BROKEN -> "No Internet connection"
                else -> "Unknown error"
            }
            activity.toast(msg)
        }
    }

    interface WeatherPresenterCallback {
        fun onLoaded(data: List<WeatherItem>);
    }
}