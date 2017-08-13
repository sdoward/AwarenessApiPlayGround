package com.sdoward.awareness.android

import com.google.android.gms.awareness.SnapshotApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.places.PlaceLikelihood
import io.reactivex.Single

fun SnapshotApi.getActivityObservable(client: GoogleApiClient): Single<List<Activity>> {
    return Single.create { emitter ->
        getDetectedActivity(client).setResultCallback {
            val activities = if (it.activityRecognitionResult.probableActivities == null) listOf<DetectedActivity>() else it.activityRecognitionResult.probableActivities
            emitter.onSuccess(activities.map { it.map() })
        }
    }
}

fun SnapshotApi.getLocationObservable(client: GoogleApiClient): Single<Location> {
    return Single.create { emitter ->
        getLocation(client).setResultCallback {
            emitter.onSuccess(it.location.map())
        }
    }
}

fun SnapshotApi.getPlacesObservable(client: GoogleApiClient): Single<List<Place>> {
    return Single.create { emitter ->
        getPlaces(client).setResultCallback {
            val placeList = if (it.placeLikelihoods == null) listOf<PlaceLikelihood>() else it.placeLikelihoods
            emitter.onSuccess(placeList.map { it.map() })
        }
    }
}