package player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import db.appDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.history.updateHistoryListWithSort
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.media.Media
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.State

object Player {
    val scope = CoroutineScope(Dispatchers.IO)

    private val player by lazy { MediaPlayerFactory().mediaPlayers().newMediaPlayer() }

    var currentMedia: String = ""
        set(value) {
            scope.launch {
                prepare(value)
                play()
            }
            field = value
        }

    var playState by mutableStateOf<State>(player.status().state())
        private set

    suspend fun prepare(mrl: String): Boolean {
        appDatabase.historyQueries.insertOrUpdateHistory(
            playTimestamp = System.currentTimeMillis(),
            path = mrl
        )
        updateHistoryListWithSort(appDatabase.historyQueries.getAllHistory().executeAsList())
        return player.media().prepare(mrl)
    }

    fun pause() {
        player.controls().pause()
    }

    fun play() {
        player.controls().play()
    }

    fun setPosition(position: Float) {
        player.controls().setPosition(position)
    }

    private val _onPositionChanged: (MediaPlayer?, Float) -> Unit = { player, newPosition ->
        onPositionChanged.invoke(player, newPosition)
    }

    var onPositionChanged: (mediaPlayer: MediaPlayer?, newPosition: Float) -> Unit = { _, _ -> }

    init {
        player.events().addMediaPlayerEventListener(object : MediaPlayerEventListenerImpl() {
            override fun positionChanged(mediaPlayer: MediaPlayer?, newPosition: Float) {
                super.positionChanged(mediaPlayer, newPosition)
                _onPositionChanged.invoke(mediaPlayer, newPosition)
            }
        })
        player.events().addMediaEventListener(object : MediaEventListenerImpl() {
            override fun mediaStateChanged(media: Media?, newState: State) {
                playState = newState
            }
        })
    }
}