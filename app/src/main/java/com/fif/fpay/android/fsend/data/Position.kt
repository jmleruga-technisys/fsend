package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Position (
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("longitude")
    var longitude: String
)