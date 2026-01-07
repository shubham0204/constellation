package io.shubham0204.model2vec.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Entity(tableName = "thoughts")
data class Thought(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val dateModifiedTimestamp: Long,
) {

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun getDateModified(): String {
        val date =
            Instant.fromEpochMilliseconds(dateModifiedTimestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        return date.format(LocalDateTime.Format { byUnicodePattern("dd/MM/yyyy, HH:mm") })
    }
}
