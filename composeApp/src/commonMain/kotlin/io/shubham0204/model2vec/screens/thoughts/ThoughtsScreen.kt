package io.shubham0204.model2vec.screens.thoughts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.BookOpen
import compose.icons.feathericons.MoreVertical
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Search
import io.shubham0204.model2vec.data.Thought
import io.shubham0204.model2vec.preview.dummyThoughts
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThoughtsScreen(
    uiState: ThoughtsScreenUiState,
    onAddThoughtClick: () -> Unit,
    onThoughtClick: (Long) -> Unit,
    onEvent: (ThoughtsScreenUiEvent) -> Unit
) {
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    FeatherIcons.BookOpen,
                                    contentDescription = null,
                                    tint = Color(0xFF7C4DFF),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Column {
                                    Text("Thoughts", style = MaterialTheme.typography.titleLarge)
                                    Text(
                                        "Your daily reflections",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF7C4DFF)
                                    )
                                }
                            }
                        },
                        actions = {
                            var showMoreOptionsPopup by remember { mutableStateOf(false) }

                            Box {
                                Icon(
                                    FeatherIcons.MoreVertical,
                                    contentDescription = "More Options",
                                    tint = Color(0xFF7C4DFF),
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .clickable { showMoreOptionsPopup = true }
                                )
                                DropdownMenu(
                                    expanded = showMoreOptionsPopup,
                                    onDismissRequest = { showMoreOptionsPopup = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Export Thoughts") },
                                        onClick = {
                                            showMoreOptionsPopup = false
                                            onEvent(ThoughtsScreenUiEvent.ExportThoughts)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Import Thoughts") },
                                        onClick = {
                                            showMoreOptionsPopup = false
                                            onEvent(ThoughtsScreenUiEvent.ImportThoughts)
                                        }
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onAddThoughtClick,
                        containerColor = Color(0xFF7C4DFF),
                        contentColor = Color.White
                    ) {
                        Icon(FeatherIcons.Plus, contentDescription = "Add Thoughts")
                    }
                },
                floatingActionButtonPosition = FabPosition.End
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { onEvent(ThoughtsScreenUiEvent.OnSearchQueryChange(it)) },
                        placeholder = { Text("Search thoughts...") },
                        leadingIcon = {
                            Icon(
                                FeatherIcons.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF7C4DFF)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7C4DFF),
                            unfocusedBorderColor = Color(0xFFBBBBBB),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                    ThoughtsList(thoughts = uiState.thoughts, onThoughtClick)
                }
            }
        }
    }

}

private enum class DateSection(val title: String) {
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month"),
    OLDER("Older")
}

private fun getDateSection(timestamp: Long): DateSection {
    val now = Clock.System.now()
    val timeZone = TimeZone.currentSystemDefault()
    val today = now.toLocalDateTime(timeZone).date
    val todayStart = today.atStartOfDayIn(timeZone).toEpochMilliseconds()
    val weekStart = today.minus(7, DateTimeUnit.DAY).atStartOfDayIn(timeZone).toEpochMilliseconds()
    val monthStart = today.minus(30, DateTimeUnit.DAY).atStartOfDayIn(timeZone).toEpochMilliseconds()
    return when {
        timestamp >= todayStart -> DateSection.TODAY
        timestamp >= weekStart -> DateSection.THIS_WEEK
        timestamp >= monthStart -> DateSection.THIS_MONTH
        else -> DateSection.OLDER
    }
}

@Composable
private fun ThoughtsList(thoughts: List<Thought>, onThoughtClick: (Long) -> Unit) {
    val groupedThoughts = thoughts.groupBy { getDateSection(it.dateModifiedTimestamp) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        DateSection.entries.forEach { section ->
            val sectionThoughts = groupedThoughts[section]
            if (!sectionThoughts.isNullOrEmpty()) {
                item {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF7C4DFF),
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(sectionThoughts) { thought ->
                    ThoughtsListItem(thought, onThoughtClick)
                }
            }
        }
    }
}

@Composable
private fun ThoughtsListItem(thought: Thought, onThoughtClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onThoughtClick(thought.id) }
            .padding(vertical = 2.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = thought.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF7C4DFF)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = thought.content,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF333333)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewThoughtsScreen() {
    ThoughtsScreen(
        uiState = ThoughtsScreenUiState(thoughts = dummyThoughts),
        onAddThoughtClick = {},
        onThoughtClick = {},
        onEvent = {}
    )
}