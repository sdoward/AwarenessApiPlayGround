package com.sdoward.awareness.android

import android.content.SharedPreferences

class UserManager(private val preferences: SharedPreferences) {

    companion object {
        val IDENTIFIER_KEY = "userIdentifier"
    }

    fun setUserIdentifier(identifier: String) {
        preferences.edit().putString(IDENTIFIER_KEY, identifier).commit()
    }

    fun haveUserIdentifier(): Boolean {
        return preferences.contains(IDENTIFIER_KEY)
    }

    fun getIdentifier(): String {
        return preferences.getString(IDENTIFIER_KEY, "")
    }

}