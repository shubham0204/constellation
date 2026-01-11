package io.shubham0204.model2vec.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtDao {
    @Insert
    suspend fun insert(thought: Thought)

    @Insert
    suspend fun insertAll(thoughts: List<Thought>)

    @Upsert
    suspend fun upsert(thought: Thought)

    @Update
    suspend fun update(thought: Thought)

    @Delete
    suspend fun delete(thought: Thought)

    @Query("SELECT * FROM Thoughts WHERE id = :id")
    suspend fun getById(id: Long): Thought

    @Query("SELECT * FROM thoughts")
    fun getAllAsFlow(): Flow<List<Thought>>

    @Query("SELECT * FROM thoughts")
    suspend fun getAll(): List<Thought>

    @Query("SELECT COUNT(*) FROM thoughts")
    suspend fun getCount(): Int
}
