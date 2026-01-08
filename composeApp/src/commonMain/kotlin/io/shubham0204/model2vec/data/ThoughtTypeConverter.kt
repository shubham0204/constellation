package io.shubham0204.model2vec.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class ThoughtTypeConverter {
    @TypeConverter
    fun floatArrayToByteArray(value: FloatArray): ByteArray {
        val byteArray = ByteArray(value.size * 4)
        for (i in value.indices) {
            val bits = value[i].toRawBits()
            byteArray[i * 4] = (bits shr 24).toByte()
            byteArray[i * 4 + 1] = (bits shr 16).toByte()
            byteArray[i * 4 + 2] = (bits shr 8).toByte()
            byteArray[i * 4 + 3] = bits.toByte()
        }
        return byteArray
    }

    @TypeConverter
    fun byteArrayToFloatArray(value: ByteArray): FloatArray {
        val floatArray = FloatArray(value.size / 4)
        for (i in floatArray.indices) {
            val bits =
                ((value[i * 4].toInt() and 0xFF) shl 24) or
                        ((value[i * 4 + 1].toInt() and 0xFF) shl 16) or
                        ((value[i * 4 + 2].toInt() and 0xFF) shl 8) or
                        (value[i * 4 + 3].toInt() and 0xFF)
            floatArray[i] = Float.fromBits(bits)
        }
        return floatArray
    }
}
