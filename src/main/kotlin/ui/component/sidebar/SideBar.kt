package ui.component.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import player.Player
import ui.BodyContentType
import util.OpenFileFilter
import util.showOpenFileDialog

@Composable
fun SideBar(bodyContentType: MutableState<BodyContentType>) {
    var expandedMenu by remember { mutableStateOf(false) }
    Column(modifier = Modifier.width(200.dp).background(MaterialTheme.colorScheme.surface)) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            SideBarItem(icon = Icons.Rounded.Home, title = "首页") {
                bodyContentType.value = BodyContentType.Home
            }
            SideBarItem(icon = Icons.Rounded.Album, title = "专辑") {
                bodyContentType.value = BodyContentType.Album
            }
            SideBarItem(icon = Icons.Rounded.History, title = "播放历史") {
                bodyContentType.value = BodyContentType.History
            }
        }
        SideBarItem(icon = Icons.Rounded.QueueMusic, title = "播放列表") {
            bodyContentType.value = BodyContentType.PlayList
        }
        SideBarItem(icon = Icons.Rounded.Menu, title = "菜单") {
            expandedMenu = !expandedMenu
        }
        MainMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false })
    }
}

@Composable
private fun SideBarItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 26.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun MainMenu(expanded: Boolean, onDismissRequest: () -> Unit) {
    val scope = rememberCoroutineScope()
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
                onDismissRequest()
                scope.launch {
                    showOpenFileDialog(
                        parent = ComposeWindow(),
                        filters = listOf(
                            OpenFileFilter("mp3;wav;m4a;flac", "音频文件"),
                            OpenFileFilter("mp3", "MP3文件"),
                            OpenFileFilter("wav", "WAV文件"),
                            OpenFileFilter("m4a", "M4A文件"),
                            OpenFileFilter("flac", "FLAC文件"),
                        )
                    ).let {
                        if (!it.isNullOrBlank()) {
                            Player.prepare(it)
                            Player.play()
                        }
                    }
                }
            },
        ) {
            Row {
                Icon(
                    imageVector = Icons.Outlined.InsertDriveFile,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "打开"
                )
            }
        }
    }
}