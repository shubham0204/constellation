package io.shubham0204.model2vec

actual class Model2Vec {
    init {
        System.loadLibrary("model2vec")
    }

    private var handle: Long = 0L
    private var numThreads: Int = 1

    actual constructor(modelPath: String, tokenizerPath: String, numThreads: Int) {
        this.handle = create(modelPath, tokenizerPath, numThreads)
        this.numThreads = numThreads
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
