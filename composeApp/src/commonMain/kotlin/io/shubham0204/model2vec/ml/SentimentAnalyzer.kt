package io.shubham0204.model2vec.ml

enum class Sentiment {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}

expect class SentimentAnalyzer {
    fun getSentimentScore(text: String): Sentiment
}