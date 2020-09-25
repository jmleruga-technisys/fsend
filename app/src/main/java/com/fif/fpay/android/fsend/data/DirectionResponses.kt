/*
 * Created by Muhammad Utsman on 31/12/2018
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 12/31/18 11:21 PM
 */

package com.fif.fpay.android.fsend.data

import com.google.gson.annotations.SerializedName
import com.utsman.samplegooglemapsdirection.kotlin.model.GeocodedWaypoint
import com.utsman.samplegooglemapsdirection.kotlin.model.Route

data class DirectionResponses(
        @SerializedName("geocoded_waypoints")
        var geocodedWaypoints: List<GeocodedWaypoint?>?,
        @SerializedName("routes")
        var routes: List<Route?>?,
        @SerializedName("status")
        var status: String?
)