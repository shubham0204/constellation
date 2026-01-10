package io.shubham0204.model2vec.screens.add_edit_thoughts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Check
import io.shubham0204.model2vec.data.Thought
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditThoughtsScreen(
    uiState: AddEditThoughtsScreenUiState,
    onBackClick: () -> Unit,
    onEvent: (AddEditThoughtsScreenEvent) -> Unit,
) {
    var title by remember { mutableStateOf(uiState.thought?.title ?: "") }
    var content by remember { mutableStateOf(uiState.thought?.content ?: "") }
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = if (uiState.thought == null) "Add Thought" else "Edit Thought") },
                    actions = {
                        IconButton(
                            onClick = {
                                onEvent(
                                    AddEditThoughtsScreenEvent.UpsertThought(
                                        Thought(
                                            id = uiState.thought?.id ?: 0L,
                                            title = title,
                                            content = content,
                                            dateModifiedTimestamp =
                                                Clock.System.now().epochSeconds,
                                        )
                                    )
                                )
                                onBackClick()
                            }
                        ) {
                            Icon(FeatherIcons.Check, contentDescription = "Update")
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(FeatherIcons.ArrowLeft, contentDescription = "Back")
                        }
                    },
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Column(modifier = Modifier.fillMaxHeight().weight(0.6f)) {
                    BasicTextField(value = title, onValueChange = { title = it })
                    BasicTextField(
                        value = content,
                        onValueChange = {
                            content = it
                            onEvent(AddEditThoughtsScreenEvent.LoadSimilarThoughts(it))
                        }
                    )
                }
                Column(modifier = Modifier.fillMaxHeight().weight(0.4f)) {
                    Text("Similar Thoughts")
                    LazyColumn {
                        items(uiState.similarThoughts) { thought ->
                            Text("${thought.title}: ${thought.content}")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddEditThoughtScreen() {
    AddEditThoughtsScreen(uiState = AddEditThoughtsScreenUiState(), onBackClick = {}, onEvent = {})
}