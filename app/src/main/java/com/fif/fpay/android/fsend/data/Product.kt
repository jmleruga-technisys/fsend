package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    var name: String,
    @SerializedName("quantity")
    var quantity: Double,
    @SerializedName("unit")
    var unit: String
)
