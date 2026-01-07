package io.shubham0204.model2vec

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import model2vec.composeapp.generated.resources.Res
import model2vec.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var results by remember { mutableStateOf("") }
        Column(
            modifier =
                Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    showContent = !showContent
                    // https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html#accessing-multiplatform-resources-from-external-libraries
                    val modelFile = FileUtils.getReadableFileFromResFileUri(Res.getUri("files/model.safetensors"))
                    val tokenizerFile = FileUtils.getReadableFileFromResFileUri(Res.getUri("files/tokenizer.json"))

                    println("tokenizerFile: $tokenizerFile")
                    println("embeddingsFile: $modelFile")

                    val model2vec = Model2Vec(modelFile, tokenizerFile, numThreads = 1)
                    val sentences = listOf("Hello, world!", "Compose Multiplatform is awesome!")
                    val embeddings = model2vec.encode(sentences)
                    model2vec.close()
                    results =
                        embeddings.joinToString(separator = "\n") {
                            it.joinToString(prefix = "[", postfix = "]")
                        }
                }
            ) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
            if (results.isNotEmpty()) {
                Text("Results: $results")
            }
        }
    }
}
