package com.sdoward.awareness.android

import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.places.PlaceLikelihood

fun DetectedActivity.map(): Activity {
    val typeString = when (type) {
        0 -> "IN_VEHICLE"
        1 -> "ON_BICYCLE"
        2 -> "ON_FOOT"
        3 -> "STILL"
        4 -> "UNKNOWN"
        5 -> "TILTING"
        7 -> "WALKING"
        8 -> "RUNNING"
        16 -> "IN_ROAD_VEHICLE"
        17 -> "IN_RAIL_VEHICLE"
        else -> "UNDEFINED"
    }
    return Activity(typeString, confidence)
}

fun android.location.Location.map(): Location {
    return Location(latitude, longitude, accuracy)
}

fun PlaceLikelihood.map(): Place {
    return Place(likelihood, place.name.toString(), place.latLng.latitude, place.latLng.longitude)
}