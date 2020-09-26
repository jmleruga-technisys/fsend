package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TimeRange (
    @SerializedName("from")
    var from: LocalDateTime,
    @SerializedName("to")
    var to: LocalDateTime
)
