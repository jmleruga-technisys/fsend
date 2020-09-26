package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName

data class Location (
    @SerializedName("Position")
    var position: Position
)