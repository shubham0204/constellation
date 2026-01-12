package io.shubham0204.model2vec.ml

import constellation.composeapp.generated.resources.Res
import io.shubham0204.model2vec.FileUtils
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier
import java.io.File


actual class SentimentAnalyzer(
    fileUtils: FileUtils
) {

    private val classifier =
        BertNLClassifier.createFromFile(
            File(fileUtils.getReadableFileFromResFileUri(Res.getUri("files/mobilebert.tflite")))
        )

    actual fun getSentimentScore(text: String): Sentiment {
        val results: MutableList<Category?>? = classifier.classify(text)
        val negativeScore = results?.get(0)?.score ?: 0f
        val positiveScore = results?.get(1)?.score ?: 0f
        if (positiveScore > negativeScore) {
            return Sentiment.POSITIVE
        } else if (negativeScore > positiveScore) {
            return Sentiment.NEGATIVE
        }
        return Sentiment.NEUTRAL
    }
}