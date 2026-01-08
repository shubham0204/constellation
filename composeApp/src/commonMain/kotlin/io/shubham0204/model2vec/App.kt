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
import model2vec.composeapp.generated.resources.Res

@Serializable
object ThoughtsScreenDestination

@Serializable
data class AddEditThoughtScreenDestination(val thoughtId: Long? = null)

@Composable
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
                        // https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html#accessing-multiplatform-resources-from-external-libraries
                        val modelFile = FileUtils.getReadableFileFromResFileUri(
                            Res.getUri("files/embeddings.safetensors")
                        )
                        val tokenizerFile = FileUtils.getReadableFileFromResFileUri(Res.getUri("files/tokenizer.json"))
                        val model2vec = Model2Vec(modelFile, tokenizerFile, numThreads = 1)
                        val embedding = model2vec.encode(listOf(newOrUpdatedThought.content))[0]
                        model2vec.close()
                        thoughtsDao.upsert(newOrUpdatedThought.copy(embedding = embedding))
                    }
                },
            )
        }
    }
}
