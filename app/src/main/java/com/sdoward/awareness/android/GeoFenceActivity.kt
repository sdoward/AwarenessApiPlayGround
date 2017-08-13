package com.sdoward.awareness.android

import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GeoFenceActivity : FragmentActivity() {

    val client: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build()
    }

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_fence_activity)
        client.connect()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.supportMapFragment) as SupportMapFragment
        mapFragment.getMap()
                .doOnSuccess { googleMap = it }
                .flatMap { Awareness.SnapshotApi.getLocationObservable(client) }
                .subscribe { (latitude, longitude, accuracy) ->
                    googleMap?.apply {
                        val currentLatLng = LatLng(latitude, longitude)
                        addCircle(CircleOptions().center(currentLatLng).radius(accuracy.toDouble()))
                        moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                    }
                }
    }

    override fun onDestroy() {
        client.disconnect()
        super.onDestroy()
    }
}
