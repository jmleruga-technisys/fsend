package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class ClientInfo (
    @SerializedName("name")
    var name: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("address")
    var address: Address,
    @SerializedName("timeRange")
    var timeRange: TimeRange
)
