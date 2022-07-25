package ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.skyd.db.History
import db.appDatabase
import kotlinx.coroutines.launch
import player.Player

var historyList: List<History> by mutableStateOf(listOf())

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        historyList = appDatabase.historyQueries.getAllHistory().executeAsList()
    }
    LazyColumn(modifier = modifier.background(Color.Green)) {
        items(items = historyList) {
            HistoryItem(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryItem(history: History) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onDoubleClick = {
                    scope.launch {
                        Player.prepare(history.path)
                        Player.play()
                    }
                }
            )
    ) {
        Text(
            text = history.path,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
