package ui.playlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import player.Player
import ui.component.TopBarIcon
import ui.component.UPlayerTopBar
import ui.component.lazyverticalgrid.LazyGridAdapter
import ui.component.lazyverticalgrid.UPlayerLazyVerticalGrid
import ui.component.lazyverticalgrid.proxy.MusicCover1Proxy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListScreen() {
    Scaffold(
        topBar = {
            UPlayerTopBar(
                title = {
                    Text(
                        text = "播放列表",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    TopBarIcon(
                        imageVector = Icons.Rounded.QueueMusic,
                        contentDescription = null
                    )
                }
            )
        }
    ) {
        val adapter: LazyGridAdapter = remember { LazyGridAdapter(mutableListOf(MusicCover1Proxy())) }
        UPlayerLazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            dataList = Player.musicList,
            adapter = adapter
        )
    }
}