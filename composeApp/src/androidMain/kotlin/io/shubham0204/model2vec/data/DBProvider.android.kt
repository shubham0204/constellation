package io.shubham0204.model2vec.data

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

actual class DBProvider(private val context: Context) {
    actual fun getDatabase(): AppDatabase {
        val dbFile = context.getDatabasePath("thoughts.db")
        return Room.databaseBuilder<AppDatabase>(context = context, name = dbFile.absolutePath)
            .addTypeConverter(ThoughtTypeConverter())
            .setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
    }
}
