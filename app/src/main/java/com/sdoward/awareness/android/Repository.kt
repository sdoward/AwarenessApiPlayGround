package com.sdoward.awareness.android

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Repository(private val userIdentifier: String) {

    val databaseReference: DatabaseReference

    init {
        val database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("awareness")
    }

    fun setData(dateTime: String, awarenessModel: AwarenessModel) {
        databaseReference.child(userIdentifier)
                .child(dateTime)
                .setValue(awarenessModel)
    }

}