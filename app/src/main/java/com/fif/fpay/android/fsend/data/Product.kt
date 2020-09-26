package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("Name")
    var name: String,
    @SerializedName("Quantity")
    var quantity: Double,
    @SerializedName("Unit")
    var unit: String,
    @SerializedName("Image")
    var image: String
)
