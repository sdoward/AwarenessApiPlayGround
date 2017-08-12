package com.sdoward.awareness.android

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        val client = GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build()
        client.connect()
    }

}
