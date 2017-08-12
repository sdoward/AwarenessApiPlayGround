package com.sdoward.awareness.android

data class Activity(val activity: String, val confidence: Int)

data class Location(val latitude: Double, val longitude: Double, val accuracy: Float)