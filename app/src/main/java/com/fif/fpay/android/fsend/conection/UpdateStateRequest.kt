package com.fif.fpay.android.fsend.conection


data class UpdateStateRequest (
    var shortcode: String,
    var state: String
)