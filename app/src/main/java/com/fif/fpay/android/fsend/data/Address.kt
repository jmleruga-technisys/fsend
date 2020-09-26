package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("position")
    var position: Position,
    @SerializedName("fullAddress")
    var fullAddress: String,
    @SerializedName("additionalInfo")
    var additionalInfo: String?
)