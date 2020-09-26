package com.fif.fpay.android.fsend.errors

import android.content.Context
import androidx.annotation.StringRes

interface ErrorEvent {

    @StringRes
    fun getErrorResource(): Int
}

fun getError(from: Context, errorEvent: ErrorEvent) = if (errorEvent.getErrorResource() == 0) {
    null
} else {
    from.getString(errorEvent.getErrorResource())
}