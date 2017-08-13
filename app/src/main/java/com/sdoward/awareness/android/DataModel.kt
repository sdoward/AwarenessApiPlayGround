package com.sdoward.awareness.android

data class Activity(val activity: String, val confidence: Int)

data class Location(val latitude: Double, val longitude: Double, val accuracy: Float)

data class Place(val likelihood: Float, val name: String, val latitude: Double, val longitude: Double)

data class AwarenessModel(val activities: List<Activity>? = null, val location: Location? = null, val places: List<Place>? = null)