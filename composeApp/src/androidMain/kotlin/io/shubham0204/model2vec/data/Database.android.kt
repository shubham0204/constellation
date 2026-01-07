package io.shubham0204.model2vec.data

import androidx.room.Room
import androidx.room.RoomDatabase
import io.shubham0204.model2vec.AndroidContextInitializer

private val applicationContext by lazy {
    AndroidContextInitializer.getContext()
}

actual val dbBuilder: RoomDatabase.Builder<AppDatabase> by lazy {
    val appContext = applicationContext.applicationContext
    val dbFile = appContext.getDatabasePath("thoughts.db")
    Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}