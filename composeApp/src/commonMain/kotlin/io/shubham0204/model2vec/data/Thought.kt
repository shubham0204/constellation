package io.shubham0204.model2vec.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.shubham0204.model2vec.ml.Sentiment
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
@Entity(tableName = "thoughts")
data class Thought(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val dateModifiedTimestamp: Long,
    val embedding: FloatArray = floatArrayOf(),
    val sentiment: Sentiment = Sentiment.NEUTRAL
) {

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun getDateModified(): String {
        val date =
            Instant.fromEpochMilliseconds(dateModifiedTimestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        return date.format(LocalDateTime.Format { byUnicodePattern("dd/MM/yyyy, HH:mm") })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Thought

        if (id != other.id) return false
        if (dateModifiedTimestamp != other.dateModifiedTimestamp) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (!embedding.contentEquals(other.embedding)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + dateModifiedTimestamp.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + embedding.contentHashCode()
        return result
    }
}
