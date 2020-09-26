package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TimeRange (
    @SerializedName("From")
    var from: String,
    @SerializedName("To")
    var to: String
)
