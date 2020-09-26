package com.fif.fpay.android.fsend.data

import com.fif.fpay.android.fsend.data.ClientInfo
import com.fif.fpay.android.fsend.data.Product
import com.google.gson.annotations.SerializedName

data class Shipment (
    @SerializedName("Products")
    var products : List<Product>,
    @SerializedName("ClientInfo")
    var clientInfo: ClientInfo,
    @SerializedName("State")
    var state: String,
    @SerializedName("Shortcode")
    var shortcode : String,
    @SerializedName("Id")
    var id: String,
    @SerializedName("UserId")
    var userId: String
)