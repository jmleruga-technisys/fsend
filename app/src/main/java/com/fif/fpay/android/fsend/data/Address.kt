package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("full_address")
    var fullAddress: String,
    @SerializedName("type")
    var type: String?,
    @SerializedName("department")
    var department: String?
)