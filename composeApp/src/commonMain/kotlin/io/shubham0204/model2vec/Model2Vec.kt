package io.shubham0204.model2vec

expect class Model2Vec {
    constructor(modelPath: String, tokenizerPath: String, numThreads: Int)

    fun encode(sentences: List<String>): List<FloatArray>

    fun close()
}
