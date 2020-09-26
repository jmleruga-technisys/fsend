package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class ClientInfo (
    @SerializedName("Name")
    var name: String,
    @SerializedName("Phone")
    var phone: String,
    @SerializedName("Address")
    var address: Address,
    @SerializedName("TimeRange")
    var timeRange: TimeRange
)
