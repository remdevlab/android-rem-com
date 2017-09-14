package org.remdev.android.kotlinapp.models

import com.google.gson.annotations.SerializedName

data class CityInfo(
        @SerializedName("title")
        var title : String,      //: "London",
        @SerializedName("location_type")
        var type : String,      //: "City",
        @SerializedName("woeid")
        var woeId : Long,      //: 44418,
        @SerializedName("latt_long")
        var latLong : String     //: "51.506321,-0.12714"
)