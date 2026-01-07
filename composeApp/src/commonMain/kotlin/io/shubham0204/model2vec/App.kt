package io.shubham0204.model2vec

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.shubham0204.model2vec.data.getRoomDatabase
import io.shubham0204.model2vec.screens.add_edit_thoughts.AddEditThoughtsScreen
import io.shubham0204.model2vec.screens.thoughts.ThoughtsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Serializable object ThoughtsScreenDestination

@Serializable data class AddEditThoughtScreenDestination(val thoughtId: Long? = null)

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val thoughtsDao = getRoomDatabase().getThoughtDao()
    NavHost(navController, startDestination = ThoughtsScreenDestination) {
        composable<ThoughtsScreenDestination> {
            ThoughtsScreen(
                thoughtsDao,
                onAddThoughtClick = { navController.navigate(AddEditThoughtScreenDestination()) },
                onThoughtClick = { thoughtId ->
                    navController.navigate(AddEditThoughtScreenDestination(thoughtId))
                },
            )
        }
        composable<AddEditThoughtScreenDestination> { backStackEntry ->
            val route = backStackEntry.toRoute<AddEditThoughtScreenDestination>()
            val thought =
                if (route.thoughtId != null) {
                    runBlocking(Dispatchers.IO) { thoughtsDao.getById(route.thoughtId) }
                } else {
                    null
                }
            AddEditThoughtsScreen(
                thought,
                onBackClick = { navController.popBackStack() },
                onUpdate = { newOrUpdatedThought ->
                    navController.popBackStack()
                    CoroutineScope(Dispatchers.IO).launch {
                        thoughtsDao.upsert(newOrUpdatedThought)
                    }
                },
            )
        }
    }

    //    MaterialTheme {
    //        var showContent by remember { mutableStateOf(false) }
    //        var results by remember { mutableStateOf("") }
    //        val dao = getRoomDatabase().getThoughtDao()
    //        val thoughts by dao.getAllAsFlow().collectAsState(initial = emptyList())
    //        Column(
    //            modifier =
    //                Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    //                    .safeContentPadding()
    //                    .fillMaxSize(),
    //            horizontalAlignment = Alignment.CenterHorizontally,
    //        ) {
    //            Button(
    //                onClick = {
    //                    showContent = !showContent
    //                    //
    // https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html#accessing-multiplatform-resources-from-external-libraries
    //                    val modelFile =
    //                        FileUtils.getReadableFileFromResFileUri(
    //                            Res.getUri("files/embeddings.safetensors")
    //                        )
    //                    val tokenizerFile =
    //
    // FileUtils.getReadableFileFromResFileUri(Res.getUri("files/tokenizer.json"))
    //
    //                    println("tokenizerFile: $tokenizerFile")
    //                    println("embeddingsFile: $modelFile")
    //
    //                    val model2vec = Model2Vec(modelFile, tokenizerFile, numThreads = 1)
    //                    val sentences = listOf("Hello, world!", "Compose Multiplatform is
    // awesome!")
    //                    val embeddings = model2vec.encode(sentences)
    //                    model2vec.close()
    //                    results =
    //                        embeddings.joinToString(separator = "\n") {
    //                            it.joinToString(prefix = "[", postfix = "]")
    //                        }
    //                }
    //            ) {
    //                Text("Click me!")
    //            }
    //            Button(
    //                onClick = {
    //                    CoroutineScope(Dispatchers.IO).launch {
    //                        dao.insert(
    //                            Thought(
    //                                title = "Hello, world!",
    //                                content = "This is a thought.",
    //                                dateModifiedTimestamp =
    // Clock.System.now().toEpochMilliseconds(),
    //                            )
    //                        )
    //                    }
    //                }
    //            ) {
    //                Text("Add Thought")
    //            }
    //            if (results.isNotEmpty()) {
    //                Text("Results: $results")
    //            }
    //            LazyColumn {
    //                items(thoughts) {
    //                    Column {
    //                        Text(it.title)
    //                        Text(it.content)
    //                        Text(it.getDateModified())
    //                    }
    //                }
    //            }
    //        }
    //    }
}
