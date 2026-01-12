package io.shubham0204.model2vec.ml

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSMakeRange
import platform.NaturalLanguage.NLTagSchemeSentimentScore
import platform.NaturalLanguage.NLTagger
import platform.NaturalLanguage.NLTokenUnit

actual class SentimentAnalyzer {
    @OptIn(ExperimentalForeignApi::class)
    actual fun getSentimentScore(text: String): Sentiment {
        /*
        https://developer.apple.com/documentation/naturallanguage/nltagger
        https://www.hackingwithswift.com/example-code/naturallanguage/how-to-perform-sentiment-analysis-on-a-string-using-nltagger
         */
        val tagger = NLTagger(listOf(NLTagSchemeSentimentScore))
        var sentiment = Sentiment.NEUTRAL
        tagger.string = text
        tagger.enumerateTagsInRange(
            range = NSMakeRange(0u, text.length.toULong()),
            unit = NLTokenUnit.NLTokenUnitParagraph,
            scheme = NLTagSchemeSentimentScore,
            options = 0u,
            usingBlock = { tag, tokenRange, _ ->
                if (tag != null) {
                    val sentimentScore = tag.toFloat()
                    sentiment = if (sentimentScore >= 0.99) {
                        Sentiment.POSITIVE
                    } else if (sentimentScore <= -0.99) {
                        Sentiment.NEGATIVE
                    } else {
                        Sentiment.NEUTRAL
                    }
                }
            }
        )
        return sentiment
    }
}