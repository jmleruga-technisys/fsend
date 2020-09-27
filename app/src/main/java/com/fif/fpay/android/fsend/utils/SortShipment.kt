package com.fif.fpay.android.fsend.utils

import com.fif.fpay.android.fsend.data.Shipment
import com.google.android.gms.maps.model.LatLng


class SortPlaces(var currentLoc: LatLng) : Comparator<Shipment?> {
    override fun compare(place1: Shipment?, place2: Shipment?): Int {
        val lat1: Double = place1!!.clientInfo.address.location.position.latitude.toDouble()
        val lon1: Double = place1!!.clientInfo.address.location.position.longitude.toDouble()
        val lat2: Double = place2!!.clientInfo.address.location.position.latitude.toDouble()
        val lon2: Double = place2!!.clientInfo.address.location.position.longitude.toDouble()
        val distanceToPlace1 =
            distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1)
        val distanceToPlace2 =
            distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2)
        return (distanceToPlace1 - distanceToPlace2).toInt()
    }

    fun distance(
        fromLat: Double,
        fromLon: Double,
        toLat: Double,
        toLon: Double
    ): Double {
        val radius = 6378137.0 // approximate Earth radius, *in meters*
        val deltaLat = toLat - fromLat
        val deltaLon = toLon - fromLon
        val angle = 2 * Math.asin(
            Math.sqrt(
                Math.pow(Math.sin(deltaLat / 2), 2.0) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                        Math.pow(Math.sin(deltaLon / 2), 2.0)
            )
        )
        return radius * angle
    }
}