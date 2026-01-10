package io.shubham0204.model2vec.screens.add_edit_thoughts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.shubham0204.model2vec.Model2Vec
import io.shubham0204.model2vec.data.AppDatabase
import io.shubham0204.model2vec.data.Thought
import io.shubham0204.model2vec.services.SimilarThoughtsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditThoughtsScreenUiState(
    val thought: Thought? = null,
    val similarThoughts: List<Thought> = emptyList()
)

sealed interface AddEditThoughtsScreenEvent {
    data class LoadThought(val thoughtId: Long) : AddEditThoughtsScreenEvent

    data class UpsertThought(val thought: Thought) : AddEditThoughtsScreenEvent

    data class LoadSimilarThoughts(val content: String) : AddEditThoughtsScreenEvent
}

class AddEditThoughtsScreenViewModel(
    private val db: AppDatabase,
    private val model2Vec: Model2Vec,
    private val similarThoughtsService: SimilarThoughtsService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditThoughtsScreenUiState())
    val uiState: StateFlow<AddEditThoughtsScreenUiState> = _uiState

    fun onEvent(event: AddEditThoughtsScreenEvent) {
        when (event) {
            is AddEditThoughtsScreenEvent.UpsertThought -> {
                viewModelScope.launch {
                    db.getThoughtDao().upsert(
                        event.thought.copy(
                            embedding = model2Vec.encode(listOf(event.thought.content))[0]
                        )
                    )
                }
            }

            is AddEditThoughtsScreenEvent.LoadSimilarThoughts -> {
                viewModelScope.launch {
                    val similarThoughts = similarThoughtsService.getSimilarThoughts(
                        event.content,
                        db.getThoughtDao().getAll().map { Pair(it.id, it.embedding) })
                    _uiState.update {
                        it.copy(
                            similarThoughts = similarThoughts.map { id ->
                                db.getThoughtDao().getById(id)
                            }
                        )
                    }
                }
            }

            is AddEditThoughtsScreenEvent.LoadThought -> {
                viewModelScope.launch {
                    val thought = db.getThoughtDao().getById(event.thoughtId)
                    _uiState.update {
                        it.copy(thought = thought)
                    }
                }
            }
        }
    }
}