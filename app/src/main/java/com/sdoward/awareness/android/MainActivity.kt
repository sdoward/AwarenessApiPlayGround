package com.sdoward.awareness.android

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)

        val client = GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build()
        client.connect()
        activitiesButton.setOnClickListener {
            Awareness.SnapshotApi.getActivityObservable(client).subscribe { activities -> infoTextView.text = activities.toString() }
        }
        locationButton.setOnClickListener {
            Awareness.SnapshotApi.getLocationObservable(client).subscribe { location -> infoTextView.text = location.toString() }
        }
        placesButton.setOnClickListener {
            Awareness.SnapshotApi.getPlacesObservable(client).subscribe { places -> infoTextView.text = places.toString() }
        }

        getAllButton.setOnClickListener {
            Single.zip(
                    Awareness.SnapshotApi.getActivityObservable(client),
                    Awareness.SnapshotApi.getLocationObservable(client),
                    Awareness.SnapshotApi.getPlacesObservable(client),
                    Function3<List<Activity>, Location, List<Place>, AwarenessModel> { activities, location, places -> AwarenessModel(activities, location, places) })
                    .subscribe { awarenessModel -> infoTextView.text = awarenessModel.toString() }

        }

    }

}
