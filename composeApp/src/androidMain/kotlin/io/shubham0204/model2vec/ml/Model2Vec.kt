package io.shubham0204.model2vec.ml

import io.shubham0204.model2vec.FileUtils
import model2vec.composeapp.generated.resources.Res

actual class Model2Vec(
    fileUtils: FileUtils
) {
    companion object {
        init {
            System.loadLibrary("model2vec")
        }
    }

    private var handle: Long = 0L
    private var numThreads: Int = 1

    init {
        val modelFile = fileUtils.getReadableFileFromResFileUri(
            Res.getUri("files/model.safetensors")
        )
        val tokenizerFile = fileUtils.getReadableFileFromResFileUri(Res.getUri("files/tokenizer.json"))
        this.handle = create(modelFile, tokenizerFile, numThreads)
    }

    actual fun encode(sentences: List<String>): List<FloatArray> {
        sentences.forEach { sentence -> addSeqBuffer(handle, sentence) }
        val embeddings = encode(handle, this.numThreads)
        clearSeqBuffer(handle)
        return embeddings.toList()
    }

    actual fun close() {
        release(this.handle)
    }

    private external fun create(
        embeddingsPath: String,
        tokenizerPath: String,
        numThreads: Int,
    ): Long

    private external fun addSeqBuffer(
        handle: Long,
        sequence: String,
    )

    private external fun clearSeqBuffer(handle: Long)

    private external fun encode(
        handle: Long,
        numThreads: Int,
    ): Array<FloatArray>

    private external fun release(handle: Long)
}