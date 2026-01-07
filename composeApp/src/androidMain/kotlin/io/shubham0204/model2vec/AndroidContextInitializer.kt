package io.shubham0204.model2vec

import android.content.Context
import java.lang.ref.WeakReference

object AndroidContextInitializer {

    private var contextRef: WeakReference<Context>? = null

    fun initialize(appContext: Context) {
        if (contextRef?.get() == null) {
            contextRef = WeakReference(appContext.applicationContext)
        }
    }

    fun getContext(): Context {
        return contextRef?.get() ?: throw IllegalStateException(
            "AndroidContextInitializer not initialized. Call AndroidContextInitializer.initialize(context) in your Application or Activity."
        )
    }
}
