package io.shubham0204.model2vec.screens.thoughts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import dummyThoughts
import io.shubham0204.model2vec.data.Thought
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThoughtsScreen(uiState: ThoughtsScreenUiState, onAddThoughtClick: () -> Unit, onThoughtClick: (Long) -> Unit) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Thoughts") },
                    actions = {}
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddThoughtClick) {
                    Icon(FeatherIcons.Plus, contentDescription = "Add Thoughts")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ThoughtsList(thoughts = uiState.thoughts, onThoughtClick)
            }
        }
    }

}

@Composable
private fun ThoughtsList(thoughts: List<Thought>, onThoughtClick: (Long) -> Unit) {
    LazyColumn {
        items(thoughts) { thought ->
            ThoughtsListItem(thought, onThoughtClick)
        }
    }
}

@Composable
private fun ThoughtsListItem(thought: Thought, onThoughtClick: (Long) -> Unit) {
    Column(modifier = Modifier.clickable { onThoughtClick(thought.id) }) {
        Text(
            text = thought.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        Text(
            text = thought.content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewThoughtsList() {
    ThoughtsList(thoughts = dummyThoughts, onThoughtClick = {})
}
