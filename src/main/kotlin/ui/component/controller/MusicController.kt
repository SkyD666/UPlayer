package ui.component.controller

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import player.Player
import uk.co.caprica.vlcj.player.base.State

@Composable
fun MusicController() {
    val scope = rememberCoroutineScope()
    var sliderPosition by remember { mutableStateOf(0f) }
    var changedValue by remember { mutableStateOf(-1f) }

    SideEffect {
        Player.onPositionChanged = { _, newPosition ->
            sliderPosition = if (changedValue < 0) newPosition else changedValue
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        Slider(
            modifier = Modifier.weight(1f),
            value = sliderPosition,
            onValueChange = {
                changedValue = it
                sliderPosition = it
            },
            valueRange = 0f..1f,
            onValueChangeFinished = {
                scope.launch {
                    Player.setPosition(changedValue)
                    changedValue = -1f
                }
            })
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious, contentDescription = null
                )
            }
            IconButton(
                onClick = {
                    scope.launch {
                        if (Player.playState == State.PLAYING) Player.pause() else Player.play()
                    }
                }
            ) {
                if (Player.playState == State.BUFFERING) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        imageVector = if (Player.playState == State.PLAYING) Icons.Rounded.Pause
                        else Icons.Rounded.PlayArrow,
                        contentDescription = null
                    )
                }
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext, contentDescription = null
                )
            }
        }
    }
}
