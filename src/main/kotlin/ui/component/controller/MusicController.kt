package ui.component.controller

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LooksOne
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import player.Player
import player.SwitchMusicProxy
import ui.component.AsyncImage
import uk.co.caprica.vlcj.media.Meta
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.State
import util.ImageUtil.loadImageBitmap
import util.toTimeString
import java.net.URI
import kotlin.math.roundToInt

@Composable
fun MusicController() {
    val scope = rememberCoroutineScope()
    var sliderPosition by remember { mutableStateOf(0f) }
    var changedValue by remember { mutableStateOf(-1f) }
    val onPositionChanged = remember<(MediaPlayer?, Float) -> Unit> {
        { _, newPosition ->
            sliderPosition = if (changedValue < 0) newPosition else changedValue
        }
    }

    LaunchedEffect(onPositionChanged) {
        Player.addOnPositionChangedListener(onPositionChanged)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MusicInfo()
        TimeLabel()
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
            }
        )
        VolumnSlider()
        val switchMusicProxies = arrayOf(
            SwitchMusicProxy.Once,
            SwitchMusicProxy.Next,
            SwitchMusicProxy.RepeatOne,
            SwitchMusicProxy.ListLoop
        )
        var switchMusicProxyIndex by remember { mutableStateOf(0) }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                switchMusicProxyIndex = (switchMusicProxyIndex + 1) % switchMusicProxies.size
                Player.switchMusicProxy = switchMusicProxies[switchMusicProxyIndex]
            }) {
                Icon(
                    imageVector = when (Player.switchMusicProxy) {
                        SwitchMusicProxy.Once -> Icons.Outlined.LooksOne
                        SwitchMusicProxy.RepeatOne -> Icons.Rounded.RepeatOne
                        SwitchMusicProxy.Next -> Icons.Rounded.Redo
                        SwitchMusicProxy.ListLoop -> Icons.Rounded.Repeat
                    },
                    contentDescription = null
                )
            }
            IconButton(onClick = { Player.previous() }) {
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
            IconButton(onClick = { Player.next() }) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext, contentDescription = null
                )
            }
        }
    }

    DisposableEffect(onPositionChanged) {
        onDispose {
            Player.removeOnPositionChangedListener(onPositionChanged)
        }
    }
}

@Composable
private fun MusicInfo() {
    if (Player.metaData != null) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Player.metaData?.get(Meta.ARTWORK_URL)?.let { artworkUrl ->
                if (!artworkUrl.startsWith("file")) return@let
                AsyncImage(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxHeight(),
                    load = {
                        var img: ImageBitmap? = null
                        runCatching {
                            img = loadImageBitmap(uri = URI(artworkUrl))
                        }.onFailure {
                            img = null
                        }
                        img
                    },
                    painterFor = {
                        it?.toAwtImage()?.toPainter() ?: rememberVectorPainter(Icons.Rounded.Image)
                    }
                )
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                println(Player.metaData?.get(Meta.ARTIST).orEmpty())
                Text(
                    modifier = Modifier.widthIn(max = 100.dp),
                    text = Player.metaData?.get(Meta.TITLE).orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                val artist = Player.metaData?.get(Meta.ARTIST)
                if (!artist.isNullOrBlank()) {
                    Text(
                        modifier = Modifier.widthIn(max = 100.dp),
                        text = artist,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeLabel() {
    var currentTime by remember { mutableStateOf(0L) }
    val onTimeChanged = remember<(MediaPlayer?, Long) -> Unit> {
        { _, newTime ->
            currentTime = newTime
        }
    }

    LaunchedEffect(Unit) {
        Player.addOnTimeChangedListener(onTimeChanged)
    }
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentTime.toTimeString(),
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            modifier = Modifier.padding(horizontal = 3.dp),
            text = "/",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = Player.duration.toTimeString(),
            style = MaterialTheme.typography.labelLarge
        )
    }

    DisposableEffect(onTimeChanged) {
        onDispose {
            Player.removeOnTimeChangedListener(onTimeChanged)
        }
    }
}

@Composable
private fun VolumnSlider() {
    val scope = rememberCoroutineScope()
    var sliderPosition by remember { mutableStateOf(Player.volume) }
    var isVolumeOff by remember { mutableStateOf(Player.isMute) }
    Player.isMute = isVolumeOff
    Row(
        modifier = Modifier.width(150.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { isVolumeOff = !isVolumeOff }) {
            Icon(
                imageVector = if (isVolumeOff) Icons.Rounded.VolumeOff
                else when (sliderPosition) {
                    0 -> Icons.Rounded.VolumeMute
                    in 1..60 -> Icons.Rounded.VolumeDown
                    in 61..100 -> Icons.Rounded.VolumeUp
                    else -> Icons.Rounded.VolumeUp
                },
                contentDescription = null
            )
        }
        Slider(
            modifier = Modifier.weight(1f),
            value = sliderPosition.toFloat(),
            onValueChange = {
                sliderPosition = it.roundToInt()
                scope.launch {
                    isVolumeOff = false
                    Player.volume = sliderPosition
                }
            },
            valueRange = 0f..200f
        )
    }
}