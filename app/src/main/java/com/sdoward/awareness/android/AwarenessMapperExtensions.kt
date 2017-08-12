package com.sdoward.awareness.android

import com.google.android.gms.location.DetectedActivity

fun DetectedActivity.map(): Activity {
    val typeString: String
    when (type) {
        0 -> typeString = "IN_VEHICLE"
        1 -> typeString = "ON_BICYCLE"
        2 -> typeString = "ON_FOOT"
        3 -> typeString = "STILL"
        4 -> typeString = "UNKNOWN"
        5 -> typeString = "TILTING"
        7 -> typeString = "WALKING"
        8 -> typeString = "RUNNING"
        16 -> typeString = "IN_ROAD_VEHICLE"
        17 -> typeString = "IN_RAIL_VEHICLE"
        else -> typeString = "UNDEFINED"
    }
    return Activity(typeString, confidence)
}