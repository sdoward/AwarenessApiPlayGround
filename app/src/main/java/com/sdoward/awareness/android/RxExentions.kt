package com.sdoward.awareness.android

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun SupportMapFragment.getMap(): Single<GoogleMap> {
    return Single.create { emmitter ->
        getMapAsync { emmitter.onSuccess(it) }
    }
}
