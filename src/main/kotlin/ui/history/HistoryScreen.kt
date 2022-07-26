package ui.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.skyd.db.History
import db.appDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.component.MessageDialog
import ui.component.TopBarIcon
import ui.component.UPlayerTopBar
import ui.component.lazyverticalgrid.LazyGridAdapter
import ui.component.lazyverticalgrid.UPlayerLazyVerticalGrid
import ui.component.lazyverticalgrid.proxy.History1Proxy

var historyList: List<History> by mutableStateOf(listOf())
    private set

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
                        text = "播放历史",
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
        val adapter: LazyGridAdapter = remember { LazyGridAdapter(mutableListOf(History1Proxy())) }
        UPlayerLazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            dataList = historyList,
            adapter = adapter
        )
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