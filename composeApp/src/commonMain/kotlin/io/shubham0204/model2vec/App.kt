package io.shubham0204.model2vec

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.shubham0204.model2vec.di.createKoinConfiguration
import io.shubham0204.model2vec.screens.add_edit_thoughts.AddEditThoughtsScreen
import io.shubham0204.model2vec.screens.add_edit_thoughts.AddEditThoughtsScreenEvent
import io.shubham0204.model2vec.screens.add_edit_thoughts.AddEditThoughtsScreenViewModel
import io.shubham0204.model2vec.screens.thoughts.ThoughtsScreen
import io.shubham0204.model2vec.screens.thoughts.ThoughtsScreenViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object ThoughtsScreenDestination

@Serializable
data class AddEditThoughtScreenDestination(val thoughtId: Long? = null)

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    // https://insert-koin.io/docs/reference/koin-compose/compose/#starting-koin-with-a-compose-app---koinapplication
    KoinMultiplatformApplication(
        config = createKoinConfiguration(),
    ) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = ThoughtsScreenDestination) {
            composable<ThoughtsScreenDestination> {
                val viewModel = koinViewModel<ThoughtsScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle(LocalLifecycleOwner.current)
                ThoughtsScreen(
                    uiState,
                    onAddThoughtClick = { navController.navigate(AddEditThoughtScreenDestination()) },
                    onThoughtClick = { thoughtId ->
                        navController.navigate(AddEditThoughtScreenDestination(thoughtId))
                    },
                    onEvent = viewModel::onEvent
                )
            }
            composable<AddEditThoughtScreenDestination> { backStackEntry ->
                val route = backStackEntry.toRoute<AddEditThoughtScreenDestination>()
                val viewModel = koinViewModel<AddEditThoughtsScreenViewModel>()
                LaunchedEffect(route.thoughtId) {
                    route.thoughtId?.let {
                        viewModel.onEvent(AddEditThoughtsScreenEvent.LoadThought(it))
                    }
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle(LocalLifecycleOwner.current)
                AddEditThoughtsScreen(
                    uiState,
                    onBackClick = { navController.popBackStack() },
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}
