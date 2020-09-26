package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Position (
    @SerializedName("Latitude")
    var latitude: String,
    @SerializedName("Longitude")
    var longitude: String
)