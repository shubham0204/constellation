package io.shubham0204.model2vec.screens.thoughts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.shubham0204.model2vec.data.AppDatabase
import io.shubham0204.model2vec.data.Thought
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class ThoughtsScreenUiState(
    val thoughts: List<Thought> = emptyList(),
)

class ThoughtsScreenViewModel(
    private val db: AppDatabase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThoughtsScreenUiState())
    val uiState: MutableStateFlow<ThoughtsScreenUiState> = _uiState

    init {
        viewModelScope.launch {
            db.getThoughtDao().getAllAsFlow().collect { thoughts ->
                _uiState.value = _uiState.value.copy(
                    thoughts = thoughts
                )
            }
        }
    }
}