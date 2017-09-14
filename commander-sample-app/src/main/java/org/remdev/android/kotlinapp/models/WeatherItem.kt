package org.remdev.android.kotlinapp.models

import com.google.gson.annotations.SerializedName

data class WeatherItem(
        @SerializedName("id")
        var id : Long, //: 5794065102667776,
        @SerializedName("weather_state_name")
        var weatherStateName : String, //: "Heavy Cloud",
        @SerializedName("weather_state_abbr")
        var weatherStateAbbr : String, //: "hc",
        @SerializedName("wind_direction_compass")
        var windDirectionCompass : String, //: "W",
        @SerializedName("created")
        var created : String, //: "2017-08-24T17:19:03.730200Z",
        @SerializedName("applicable_date")
        var applicableDate : String, //: "2017-08-24",
        @SerializedName("min_temp")
        var minTemperature : Double, //: 13.678333333333333,
        @SerializedName("max_temp")
        var maxTemperature : Double, //: 21.111666666666668,
        @SerializedName("the_temp")
        var currentTemperature : Double, //: 20.13,
        @SerializedName("wind_speed")
        var windSpeed : Double, //: 6.850392834319574,
        @SerializedName("wind_direction")
        var windDirection : Double, //: 261.52878159175776,
        @SerializedName("air_pressure")
        var airPressure : Double, //: 1019.875,
        @SerializedName("humidity")
        var humidity : Double, //: 64,
        @SerializedName("visibility")
        var visibility : Double, //: 11.021260836713592,
        @SerializedName("predictability")
        var predictability : Double //: 71
)
