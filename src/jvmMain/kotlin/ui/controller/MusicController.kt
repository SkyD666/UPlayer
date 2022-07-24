package ui.controller

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.co.caprica.vlcj.factory.MediaPlayerFactory


val player by lazy {
    MediaPlayerFactory().mediaPlayers().newMediaPlayer()
}
var isPlaying by mutableStateOf(false)
val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

@Composable
fun MusicController() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Row(modifier = Modifier.fillMaxWidth()) {
        Slider(modifier = Modifier.weight(1f),
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            })
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious, contentDescription = null
                )
            }
            IconButton(onClick = {
                coroutineScope.launch {
                    player.media().play("D:\\music.mp3")
                }
            }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = null
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext, contentDescription = null
                )
            }
        }
    }
}
