package com.sdoward.awareness.android

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {


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
            Awareness.SnapshotApi.getDetectedActivity(client).setResultCallback {
                infoTextView.text = it.activityRecognitionResult
                        .probableActivities
                        .map { it.map() }
                        .toString()
            }
        }
        locationButton.setOnClickListener {
            Awareness.SnapshotApi.getLocation(client).setResultCallback {
                infoTextView.text = it.location.map().toString()
            }
        }
        placesButton.setOnClickListener {
            Awareness.SnapshotApi.getPlaces(client).setResultCallback {
                if (it.placeLikelihoods == null) {
                    infoTextView.text = "No places detected"
                } else {
                    infoTextView.text = it.placeLikelihoods
                            .map { it.map() }
                            .toString()

                }

            }
        }

    }

}
