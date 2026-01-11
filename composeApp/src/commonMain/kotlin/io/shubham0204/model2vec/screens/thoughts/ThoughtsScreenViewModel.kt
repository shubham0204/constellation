package io.shubham0204.model2vec.screens.thoughts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.shubham0204.model2vec.Model2Vec
import io.shubham0204.model2vec.data.AppDatabase
import io.shubham0204.model2vec.data.Thought
import io.shubham0204.model2vec.preview.dummyThoughts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.sqrt

data class ThoughtsScreenUiState(
    val thoughts: List<Thought> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
)

sealed interface ThoughtsScreenUiEvent {
    data class OnSearchQueryChange(val query: String) : ThoughtsScreenUiEvent
}

class ThoughtsScreenViewModel(
    private val db: AppDatabase,
    private val model2vec: Model2Vec
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThoughtsScreenUiState())
    val uiState: MutableStateFlow<ThoughtsScreenUiState> = _uiState

    private var allThoughts: List<Thought> = emptyList()

    init {
        viewModelScope.launch {
            if (db.getThoughtDao().getCount() == 0) {
                val embeddings = model2vec.encode(dummyThoughts.map { it.content })
                db.getThoughtDao()
                    .insertAll(dummyThoughts.zip(embeddings).map { (thought, embedding) ->
                        thought.copy(embedding = embedding)
                    })
            }
            db.getThoughtDao().getAllAsFlow().collect { thoughts ->
                allThoughts = thoughts
                if (_uiState.value.searchQuery.isEmpty()) {
                    _uiState.value = _uiState.value.copy(thoughts = thoughts)
                } else {
                    performSearch(_uiState.value.searchQuery)
                }
            }
        }
    }

    fun onEvent(event: ThoughtsScreenUiEvent) {
        when (event) {
            is ThoughtsScreenUiEvent.OnSearchQueryChange -> {
                _uiState.value = _uiState.value.copy(searchQuery = event.query)
                if (event.query.isEmpty()) {
                    _uiState.value = _uiState.value.copy(thoughts = allThoughts, isSearching = false)
                } else {
                    performSearch(event.query)
                }
            }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)

            // Text-based search (case-insensitive)
            val textMatches = allThoughts.filter { thought ->
                thought.title.contains(query, ignoreCase = true) ||
                        thought.content.contains(query, ignoreCase = true)
            }.map { it.id to 1.0f }.toMap()

            // Semantic search using embeddings
            val queryEmbedding = model2vec.encode(listOf(query)).firstOrNull()
            val semanticScores = if (queryEmbedding != null) {
                allThoughts.mapNotNull { thought ->
                    thought.embedding?.let { embedding ->
                        thought.id to cosineSimilarity(queryEmbedding, embedding)
                    }
                }.toMap()
            } else {
                emptyMap()
            }

            // Combine results: prioritize text matches, then sort by semantic similarity
            val combinedResults = allThoughts
                .map { thought ->
                    val textScore = if (textMatches.containsKey(thought.id)) 1.0f else 0.0f
                    val semanticScore = semanticScores[thought.id] ?: 0.0f
                    // Combined score: text match gets higher weight
                    thought to (textScore * 2.0f + semanticScore)
                }
                .filter { (_, score) -> score > 0.3f } // Filter low relevance results
                .sortedByDescending { (_, score) -> score }
                .map { (thought, _) -> thought }

            _uiState.value = _uiState.value.copy(
                thoughts = combinedResults,
                isSearching = false
            )
        }
    }

    private fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        if (a.size != b.size) return 0.0f
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f
        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        val denominator = sqrt(normA) * sqrt(normB)
        return if (denominator == 0.0f) 0.0f else dotProduct / denominator
    }
}