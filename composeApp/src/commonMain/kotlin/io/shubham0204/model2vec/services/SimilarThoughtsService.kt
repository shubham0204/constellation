package io.shubham0204.model2vec.services

import io.shubham0204.model2vec.ml.Model2Vec
import kotlin.math.sqrt

class SimilarThoughtsService(
    private val model2vec: Model2Vec,
) {

    fun getSimilarThoughts(
        thoughtContent: String,
        allThoughtEmbeddings: List<Pair<Long, FloatArray>>,
        topK: Int = 5,
    ): List<Long> {
        val thoughtEmbedding = model2vec.encode(listOf(thoughtContent)).first()
        val similarities = allThoughtEmbeddings.map { (id, embedding) ->
            val similarity = cosineSimilarity(thoughtEmbedding, embedding)
            id to similarity
        }
        return similarities
            .sortedByDescending { it.second }
            .take(topK)
            .map { it.first }
    }

    private fun cosineSimilarity(x1: FloatArray, x2: FloatArray): Float {
        require(x1.size == x2.size) { "Vectors must be of the same length" }
        var dotProduct = 0.0f
        var normX1 = 0.0f
        var normX2 = 0.0f
        for (i in x1.indices) {
            dotProduct += x1[i] * x2[i]
            normX1 += x1[i] * x1[i]
            normX2 += x2[i] * x2[i]
        }
        return if (normX1 == 0.0f || normX2 == 0.0f) {
            0.0f
        } else {
            dotProduct / (sqrt(normX1) * sqrt(normX2))
        }
    }
}