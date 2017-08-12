package com.sdoward.awareness.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)

        val client = GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build()
        client.connect()
        activitiesButton.setOnClickListener {
            Awareness.SnapshotApi.getDetectedActivity(client).setResultCallback {
                val activityList = it.activityRecognitionResult
                        .probableActivities
                        .map { it.map() }
                activitiesTextView.text = activityList.toString()
            }
        }
    }

}
