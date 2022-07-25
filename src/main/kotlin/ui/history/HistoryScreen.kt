package ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.skyd.db.History
import db.appDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import player.Player
import ui.component.MessageDialog
import ui.component.TopBarIcon
import ui.component.UPlayerTopBar
import util.awaitEventFirstDown
import util.time2Now
import util.toTimeString
import java.awt.event.MouseEvent

private var historyList: List<History> by mutableStateOf(listOf())

fun updateHistoryListWithSort(list: List<History>) {
    historyList = list.sortedBy { -it.playTimestamp }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    val scope = rememberCoroutineScope()
    var showWarningDeleteDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        updateHistoryListWithSort(appDatabase.historyQueries.getAllHistory().executeAsList())
    }
    Scaffold(
        topBar = {
            UPlayerTopBar(
                title = {
                    Text(
                        text = "历史记录",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    TopBarIcon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = null
                    )
                },
                actions = {
                    TopBarIcon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                        onClick = { showWarningDeleteDialog = true }
                    )
                }
            )
        }
    ) {
        LazyColumn {
            items(items = historyList) {
                HistoryItem(it)
            }
        }
    }
    if (showWarningDeleteDialog) {
        MessageDialog(
            icon = Icons.Rounded.Warning,
            message = "确定要删除所有历史记录吗？",
            onNegative = {
                showWarningDeleteDialog = false
            },
            onPositive = {
                showWarningDeleteDialog = false
                scope.launch(Dispatchers.IO) {
                    appDatabase.historyQueries.deleteAllHistories()
                    updateHistoryListWithSort(emptyList())
                }
            },
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryItem(history: History) {
    val scope = rememberCoroutineScope()
    var lastEvent by remember { mutableStateOf<MouseEvent?>(null) }
    var expandedMenu by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        lastEvent = awaitEventFirstDown().apply {
                            changes.forEach { it.consumeDownChange() }
                        }.awtEventOrNull
                        if (lastEvent?.button == MouseEvent.BUTTON3) {
                            expandedMenu = true
                        }
                    }
                }
            }
            .combinedClickable(
                onClick = {},
                onDoubleClick = {
                    scope.launch {
                        Player.prepare(history.path)
                        Player.play()
                    }
                },
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HistoryItemMenu(
            history = history,
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false }
        )
        Text(
            modifier = Modifier.weight(1f),
            text = history.path,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = history.playTimestamp.time2Now(),
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
                androidx.compose.material3.Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "删除"
                )
            }
        }
    }
}