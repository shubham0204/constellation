@file:OptIn(ExperimentalForeignApi::class)

package io.shubham0204.model2vec

import io.shubham0204.model2vec.native.model2vec_add_seq_buffer
import io.shubham0204.model2vec.native.model2vec_clear_seq_buffer
import io.shubham0204.model2vec.native.model2vec_create
import io.shubham0204.model2vec.native.model2vec_encode_seq_buffer
import io.shubham0204.model2vec.native.model2vec_release
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.useContents
import model2vec.composeapp.generated.resources.Res

actual class Model2Vec(
    private val fileUtils: FileUtils
) {

    private var handle: Long = 0L
    private val numThreads = 1L

    init {
        memScoped {
            val modelFile = fileUtils.getReadableFileFromResFileUri(
                Res.getUri("files/embeddings.safetensors")
            )
            val tokenizerFile = fileUtils.getReadableFileFromResFileUri(Res.getUri("files/tokenizer.json"))
            handle =
                model2vec_create(modelFile, tokenizerFile, numThreads.toULong())?.rawValue?.toLong()
                    ?: 0L
        }
    }

    actual fun encode(sentences: List<String>): List<FloatArray> {
        sentences.forEach { sentence -> model2vec_add_seq_buffer(handle.toCPointer(), sentence) }
        val cEmbeddingsArray = model2vec_encode_seq_buffer(handle.toCPointer())
        val embeddingsArray = cEmbeddingsArray.useContents { data }
        val embeddingsArrayLen = cEmbeddingsArray.useContents { len }
        val result = mutableListOf<FloatArray>()
        for (i in 0 until embeddingsArrayLen) {
            val cEmbedding = embeddingsArray?.get(i) ?: continue
            val embedding = cEmbedding.data
            val embeddingLen = cEmbedding.len
            val floatArray = FloatArray(embeddingLen)
            for (j in 0 until embeddingLen) {
                floatArray[j] = embedding?.get(j) ?: 0f
            }
            result.add(floatArray)
        }
        model2vec_clear_seq_buffer(handle.toCPointer())
        return result
    }

    actual fun close() {
        model2vec_release(handle.toCPointer())
        handle = 0L
    }
}
