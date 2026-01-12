package io.shubham0204.model2vec.ml

expect class Model2Vec {
    fun encode(sentences: List<String>): List<FloatArray>

    fun close()
}