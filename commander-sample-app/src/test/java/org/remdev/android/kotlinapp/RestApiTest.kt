package org.remdev.android.kotlinapp

import org.junit.Assert
import org.junit.Test
import org.remdev.android.kotlinapp.rest.WeatherAPI
import org.remdev.android.kotlinapp.rest.buildRestApi

class RestApiTest {

    @Test
    @Throws(Exception::class)
    fun testLoadWoiedForMinsk() {
        val weatherApi = buildRestApi("https://www.metaweather.com", WeatherAPI::class.java);

        val cities = weatherApi.findCity("Minsk").execute()
        Assert.assertTrue(cities.isSuccessful)
        val citiesList = cities.body()
        Assert.assertTrue(citiesList.isNotEmpty())
        Assert.assertTrue(citiesList.first().title == "Minsk")
    }

    @Test
    @Throws(Exception::class)
    fun testLoadForMinsk() {
        val weatherApi = buildRestApi("https://www.metaweather.com", WeatherAPI::class.java);

        val cities = weatherApi.findCity("Minsk").execute()
        Assert.assertTrue(cities.isSuccessful)
        val citiesList = cities.body()
        Assert.assertTrue(citiesList.isNotEmpty())
        val minskData = citiesList.first()
        Assert.assertTrue(minskData.title == "Minsk")

        val weatherDataResp = weatherApi.loadWeather(minskData.woeId).execute()
        Assert.assertTrue(weatherDataResp.isSuccessful)
        val dayItems = weatherDataResp.body().items
        Assert.assertTrue(dayItems.isNotEmpty())
        dayItems.forEach { println(it) }
    }
}