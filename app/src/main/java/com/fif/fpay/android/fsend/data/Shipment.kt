package com.fif.fpay.android.fsend.data

import com.fif.fpay.android.fsend.data.ClientInfo
import com.fif.fpay.android.fsend.data.Product
import com.google.gson.annotations.SerializedName

data class Shipment (
    @SerializedName("products")
    var products : ArrayList<Product>,
    @SerializedName("clientInfo")
    var clientInfo: ClientInfo,
    @SerializedName("state")
    var state: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("userId")
    var userId: String
)