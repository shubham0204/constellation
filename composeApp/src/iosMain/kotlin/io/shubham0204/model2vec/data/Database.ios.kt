package io.shubham0204.model2vec.data

import androidx.room.Room
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DBProvider {

    actual fun getDatabase(): AppDatabase {
        val dbFilePath = documentDirectory() + "/thoughts.db"
        return Room.databaseBuilder<AppDatabase>(name = dbFilePath)
            .addTypeConverter(ThoughtTypeConverter())
            .setDriver(androidx.sqlite.driver.bundled.BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory =
            NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
        return requireNotNull(documentDirectory?.path)
    }
}

