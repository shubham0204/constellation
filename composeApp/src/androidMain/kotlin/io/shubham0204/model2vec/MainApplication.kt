package io.shubham0204.model2vec

import android.app.Application

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidContextInitializer.initialize(this)
    }
}
