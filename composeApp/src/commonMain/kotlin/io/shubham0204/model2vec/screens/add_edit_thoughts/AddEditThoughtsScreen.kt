package io.shubham0204.model2vec.screens.add_edit_thoughts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Check
import compose.icons.feathericons.Delete
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
    var title by rememberSaveable { mutableStateOf(uiState.thought?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(uiState.thought?.content ?: "") }
    LaunchedEffect(uiState.thought) {
        uiState.thought?.let { thought ->
            title = thought.title
            content = thought.content
            onEvent(AddEditThoughtsScreenEvent.LoadSimilarThoughts(thought.content))
        }
    }
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F0FF), Color(0xFFF8E8FF))
                    )
                )
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (uiState.thought == null) "Add Thought" else "Edit Thought",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF7C4DFF)
                            )
                        },
                        actions = {
                            if (uiState.thought != null) {
                                IconButton(
                                    onClick = {
                                        onEvent(AddEditThoughtsScreenEvent.DeleteThought(uiState.thought.id))
                                        onBackClick()
                                    }
                                ) {
                                    Icon(
                                        FeatherIcons.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(0xFF7C4DFF)
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    onEvent(
                                        AddEditThoughtsScreenEvent.UpsertThought(
                                            Thought(
                                                id = uiState.thought?.id ?: 0L,
                                                title = title,
                                                content = content,
                                                dateModifiedTimestamp =
                                                    Clock.System.now().toEpochMilliseconds(),
                                            )
                                        )
                                    )
                                    onBackClick()
                                }
                            ) {
                                Icon(
                                    FeatherIcons.Check,
                                    contentDescription = "Update",
                                    tint = Color(0xFF7C4DFF)
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    FeatherIcons.ArrowLeft,
                                    contentDescription = "Back",
                                    tint = Color(0xFF7C4DFF)
                                )
                            }
                        },
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Title",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color(0xFF7C4DFF)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BasicTextField(
                                value = title,
                                onValueChange = { title = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF3F3F3), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF333333))
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Content",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color(0xFF7C4DFF)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BasicTextField(
                                value = content,
                                onValueChange = {
                                    content = it
                                    onEvent(AddEditThoughtsScreenEvent.LoadSimilarThoughts(it))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF3F3F3), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                                    .height(100.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF333333))
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                "Similar Thoughts",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF7C4DFF)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (uiState.similarThoughts.isEmpty()) {
                                Text(
                                    "No similar thoughts found.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF999999)
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(uiState.similarThoughts.filter { it.id != uiState.thought?.id }) { thought ->
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8E8FF)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Text(
                                                    thought.title,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = Color(0xFF7C4DFF)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    thought.content,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color(0xFF333333)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
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