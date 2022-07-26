package ui.component.lazyverticalgrid.proxy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skyd.db.History
import db.appDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import player.Player
import ui.component.lazyverticalgrid.LazyGridAdapter
import ui.history.updateHistoryListWithSort
import util.time2Now

class History1Proxy : LazyGridAdapter.Proxy<History>() {
    @Composable
    override fun Draw(modifier: Modifier, index: Int, data: History) {
        History1Item(data = data)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun History1Item(data: History) {
    val scope = rememberCoroutineScope()
    var expandedMenu by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onDoubleClick = {
                    scope.launch {
                        Player.prepare(data.path)
                        Player.play()
                    }
                },
                onLongClick = { expandedMenu = true }
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HistoryItemMenu(
            history = data,
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false }
        )
        Text(
            modifier = Modifier.weight(1f),
            text = data.path,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = data.playTimestamp.time2Now(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun HistoryItemMenu(history: History, expanded: Boolean, onDismissRequest: () -> Unit) {
    val scope = rememberCoroutineScope()
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
                onDismissRequest()
                scope.launch(Dispatchers.IO) {
                    appDatabase.historyQueries.deleteHistory(path = history.path)
                    updateHistoryListWithSort(
                        appDatabase.historyQueries.getAllHistory().executeAsList()
                    )
                }
            },
        ) {
            Row {
                Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "删除"
                )
            }
        }
    }
}