package com.sdoward.awareness.android

import android.app.PendingIntent
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.*
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.geo_fence_activity.*
import android.util.TypedValue
import android.widget.Toast
import com.google.android.gms.awareness.fence.AwarenessFence
import com.google.android.gms.awareness.fence.FenceUpdateRequest
import com.google.android.gms.awareness.fence.LocationFence
import android.content.Intent

class GeoFenceActivity : FragmentActivity() {

    private val client: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build()
    }
    private var googleMap: GoogleMap? = null
    private val bottomSheetBehaviour: BottomSheetBehavior<LinearLayout> by lazy {
        from(bottomSheetLayout)
    }
    private var latLng = LatLng(0.0, 0.0)//null island
    val awarenessIntent: PendingIntent by lazy {
        val intent = Intent("com.sdoward.awareness.android.FENCE")
        PendingIntent.getBroadcast(this, 0, intent, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_fence_activity)
        bottomSheetBehaviour.state = STATE_HIDDEN;
        client.connect()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.supportMapFragment) as SupportMapFragment
        mapFragment.getMap()
                .doOnSuccess { googleMap = it }
                .flatMap { Awareness.SnapshotApi.getLocationObservable(client) }
                .subscribe { (latitude, longitude, accuracy) ->
                    googleMap?.apply {
                        isMyLocationEnabled = true
                        val currentLatLng = LatLng(latitude, longitude)
                        moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
                        setOnMapClickListener {
                            latLng = it
                            moveCamera(CameraUpdateFactory.newLatLng(it))
                            bottomSheetBehaviour.state = STATE_EXPANDED
                        }
                    }
                }

        enteringRadioButton.setOnClickListener { dwellLayout.visibility = View.GONE }
        existingRadioButton.setOnClickListener { dwellLayout.visibility = View.GONE }
        dwellRadioButton.setOnClickListener { dwellLayout.visibility = View.VISIBLE }
        addButton.setOnClickListener {
            if (nameEditText.text.isEmpty()) {
                Toast.makeText(this, "Add a name of the geo fence", Toast.LENGTH_LONG).show()
            } else {
                val locationFence: AwarenessFence
                if (enteringRadioButton.isChecked) {
                    locationFence = LocationFence.entering(latLng.latitude, latLng.latitude, radiusEditText.text.toString().toDouble())
                } else if (existingRadioButton.isChecked) {
                    locationFence = LocationFence.exiting(latLng.latitude, latLng.latitude, radiusEditText.text.toString().toDouble())
                } else {
                    locationFence = LocationFence.`in`(latLng.latitude, latLng.latitude, radiusEditText.text.toString().toDouble(), dwellEditText.text.toString().toLong())
                }
                Awareness.FenceApi.updateFences(client, FenceUpdateRequest.Builder()
                        .addFence(nameEditText.text.toString(), locationFence, awarenessIntent)
                        .build())
                Toast.makeText(this, "Geo fence added", Toast.LENGTH_LONG).show()
                bottomSheetBehaviour.state = STATE_HIDDEN
            }
        }
    }

    fun convertToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    override fun onDestroy() {
        client.disconnect()
        super.onDestroy()
    }

}
