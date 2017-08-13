package com.sdoward.awareness.android

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.main_activity.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val repository: Repository by lazy {
        Repository(UserManager(PreferenceManager.getDefaultSharedPreferences(this)).getIdentifier())
    }
    val client: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build()
    }
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        client.connect()
        activitiesButton.setOnClickListener {
            Awareness.SnapshotApi.getActivityObservable(client)
                    .map { AwarenessModel(it) }
                    .doOnSuccess { repository.setData(getDateTime(), it) }
                    .subscribe { activities -> infoTextView.text = activities.toString() }
                    .addTo(disposable)
        }
        locationButton.setOnClickListener {
            Awareness.SnapshotApi.getLocationObservable(client)
                    .map { AwarenessModel(location = it) }
                    .doOnSuccess { repository.setData(getDateTime(), it) }
                    .subscribe { location -> infoTextView.text = location.toString() }
                    .addTo(disposable)
        }
        placesButton.setOnClickListener {
            Awareness.SnapshotApi.getPlacesObservable(client)
                    .map { AwarenessModel(places = it) }
                    .doOnSuccess { repository.setData(getDateTime(), it) }
                    .subscribe { places -> infoTextView.text = places.toString() }
                    .addTo(disposable)
        }

        getAllButton.setOnClickListener {
            Single.zip(
                    Awareness.SnapshotApi.getActivityObservable(client),
                    Awareness.SnapshotApi.getLocationObservable(client),
                    Awareness.SnapshotApi.getPlacesObservable(client),
                    Function3<List<Activity>, Location, List<Place>, AwarenessModel> { activities, location, places -> AwarenessModel(activities, location, places) })
                    .doOnSuccess { repository.setData(getDateTime(), it) }
                    .subscribe { awarenessModel -> infoTextView.text = awarenessModel.toString() }
                    .addTo(disposable)
        }

        geoFenceButton.setOnClickListener { startActivity(Intent(this, GeoFenceActivity::class.java)) }

    }

    fun getDateTime() = dateFormat.format(Date())

    override fun onDestroy() {
        client.disconnect()
        disposable.clear()
        super.onDestroy()
    }
}
