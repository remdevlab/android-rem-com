package org.remdev.android.kotlinapp.rest

import org.remdev.android.kotlinapp.models.CityInfo
import org.remdev.android.kotlinapp.models.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/api/location/search/")
    fun findCity(@Query("query") name : String) : Call<List<CityInfo>>

    @GET("/api/location/{woeid}/")
    fun loadWeather(@Path("woeid") woeId : Long) : Call<WeatherData>
}