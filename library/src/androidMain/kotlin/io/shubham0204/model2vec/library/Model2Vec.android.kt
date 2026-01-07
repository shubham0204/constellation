package io.shubham0204.model2vec.library

actual class Model2Vec {
    actual fun encode(sentences: List<String>): List<FloatArray> {
        // Android-specific implementation goes here
        return sentences.map { sentence ->
            // Dummy implementation: convert each character to its ASCII value as Float
            sentence.map { it.code.toFloat() }.toFloatArray()
        }
    }

    actual fun close() {}

    actual constructor(modelPath: String, tokenizerPath: String, numThreads: Int) {
        TODO("Not yet implemented")
    }
}
