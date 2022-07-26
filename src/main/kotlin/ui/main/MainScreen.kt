package ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ui.BodyContentType
import ui.component.controller.MusicController
import ui.component.sidebar.SideBar
import ui.history.HistoryScreen
import ui.playlist.PlayListScreen

@Composable
fun MainScreen() {
    val bodyContentType = remember { mutableStateOf(BodyContentType.Home) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SideBar(bodyContentType = bodyContentType)
            BodyContent(bodyContentType = bodyContentType.value, modifier = Modifier.weight(1f))

        }
        MusicController()
    }
}

@Composable
fun BodyContent(bodyContentType: BodyContentType, modifier: Modifier = Modifier) {
    Crossfade(
        modifier = modifier.fillMaxHeight(),
        targetState = bodyContentType
    ) {
        when (it) {
            BodyContentType.History -> HistoryScreen()
            BodyContentType.PlayList -> PlayListScreen()
        }
    }
}