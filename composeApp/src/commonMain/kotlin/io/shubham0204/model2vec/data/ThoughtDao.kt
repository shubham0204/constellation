package io.shubham0204.model2vec.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtDao {
    @Insert suspend fun insert(thought: Thought)

    @Update suspend fun update(thought: Thought)

    @Delete suspend fun delete(thought: Thought)

    @Query("SELECT * FROM thoughts") fun getAllAsFlow(): Flow<List<Thought>>
}
