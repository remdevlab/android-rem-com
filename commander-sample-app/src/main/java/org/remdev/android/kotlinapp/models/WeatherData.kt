package org.remdev.android.kotlinapp.models

import com.google.gson.annotations.SerializedName

data class WeatherData(
        @SerializedName("consolidated_weather") var items : List<WeatherItem> = emptyList(),
        @SerializedName("details") var details : String?
)
