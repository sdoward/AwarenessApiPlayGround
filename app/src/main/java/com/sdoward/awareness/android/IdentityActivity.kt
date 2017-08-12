package com.sdoward.awareness.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.identity_activity.*


class IdentityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.identity_activity)
        val  userManager = UserManager(PreferenceManager.getDefaultSharedPreferences(this))
        if (userManager.haveUserIdentifier()) {
            navigateToMainActivity()
        } else {
            goButton.setOnClickListener {
                if (identityEditText.text == null) {
                    Toast.makeText(this, "Be cool, tell us who you are", Toast.LENGTH_LONG).show()
                } else {
                    userManager.setUserIdentifier(identityEditText.text.toString())
                    navigateToMainActivity()
                }
            }
        }
    }

    fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
