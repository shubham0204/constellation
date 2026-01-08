package io.shubham0204.model2vec.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [Thought::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(ThoughtTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getThoughtDao(): ThoughtDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect val dbBuilder: RoomDatabase.Builder<AppDatabase>

fun getRoomDatabase(): AppDatabase {
    return dbBuilder.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}
