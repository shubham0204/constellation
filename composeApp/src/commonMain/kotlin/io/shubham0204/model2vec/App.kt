package io.shubham0204.model2vec

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.shubham0204.model2vec.data.Thought
import io.shubham0204.model2vec.data.getRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import model2vec.composeapp.generated.resources.Res
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var results by remember { mutableStateOf("") }
        val dao = getRoomDatabase().getThoughtDao()
        val thoughts by dao.getAllAsFlow().collectAsState(initial = emptyList())
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
                    val modelFile =
                        FileUtils.getReadableFileFromResFileUri(
                            Res.getUri("files/embeddings.safetensors")
                        )
                    val tokenizerFile =
                        FileUtils.getReadableFileFromResFileUri(Res.getUri("files/tokenizer.json"))

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
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.insert(
                            Thought(
                                title = "Hello, world!",
                                content = "This is a thought.",
                                dateModifiedTimestamp = Clock.System.now().toEpochMilliseconds(),
                            )
                        )
                    }
                }
            ) {
                Text("Add Thought")
            }
            if (results.isNotEmpty()) {
                Text("Results: $results")
            }
            LazyColumn {
                items(thoughts) {
                    Column {
                        Text(it.title)
                        Text(it.content)
                        Text(it.getDateModified())
                    }
                }
            }
        }
    }
}
