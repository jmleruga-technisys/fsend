package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("Location")
    var location: Location,
    @SerializedName("FullAddress")
    var fullAddress: String,
    @SerializedName("AdditionalInfo")
    var additionalInfo: String?
)