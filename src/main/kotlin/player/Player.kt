package player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bean.MusicCover1Bean
import db.appDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.history.updateHistoryListWithSort
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.media.Media
import uk.co.caprica.vlcj.media.MediaEventAdapter
import uk.co.caprica.vlcj.media.Meta
import uk.co.caprica.vlcj.media.MetaData
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.base.State
import java.io.File
import java.lang.Integer.max
import java.nio.charset.Charset
import kotlin.math.min

object Player {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val player by lazy { MediaPlayerFactory().mediaPlayers().newMediaPlayer() }

    var switchMusicProxy: SwitchMusicProxy by mutableStateOf(SwitchMusicProxy.Once)

    var musicList: MutableList<MusicCover1Bean> = mutableListOf()

    var musicIndex: Int = 0

    var playState by mutableStateOf<State>(player.status().state())
        private set

    suspend fun prepare(mrl: String): Boolean {
        appDatabase.historyQueries.insertOrUpdateHistory(
            playTimestamp = System.currentTimeMillis(),
            path = mrl
        )
        updateHistoryListWithSort(appDatabase.historyQueries.getAllHistory().executeAsList())
        musicList.forEachIndexed { index, s -> if (s.path == mrl) musicIndex = index }
        return player.media().prepare(mrl)
    }

    fun release() {
        player.release()
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

    fun previousOrLast() {
        if (musicList.isEmpty()) return
        scope.launch {
            musicIndex = (musicIndex - 1) % musicList.size
            prepare(musicList[musicIndex].path)
            play()
        }
    }

    fun previous() {
        if (musicList.isEmpty()) return
        scope.launch {
            musicIndex = max(musicIndex - 1, 0)
            prepare(musicList[musicIndex].path)
            play()
        }
    }

    fun next() {
        if (musicList.isEmpty()) return
        scope.launch {
            musicIndex = min(musicIndex + 1, musicList.size - 1)
            prepare(musicList[musicIndex].path)
            play()
        }
    }

    fun nextOrFirst() {
        if (musicList.isEmpty()) return
        scope.launch {
            musicIndex = (musicIndex + 1) % musicList.size
            prepare(musicList[musicIndex].path)
            play()
        }
    }

    var volume: Int = player.audio().volume()
        set(value) {
            player.audio().setVolume(min(200, max(0, value)))
        }

    var isMute: Boolean = player.audio().isMute
        set(value) {
            player.audio().isMute = value
            field = value
        }

    var metaData: MetaData? by mutableStateOf(null)
        private set

    var duration: Long by mutableStateOf(player.media().info()?.duration() ?: 0L)
        private set

    private val _onPositionChanged: (MediaPlayer?, Float) -> Unit = { player, newPosition ->
        onPositionChangedList.forEach {
            it.invoke(player, newPosition)
        }
    }

    private var onPositionChangedList: MutableList<(MediaPlayer?, Float) -> Unit> =
        mutableListOf()

    fun addOnPositionChangedListener(listener: (MediaPlayer?, Float) -> Unit) {
        onPositionChangedList.add(listener)
    }

    fun removeOnPositionChangedListener(listener: (MediaPlayer?, Float) -> Unit) {
        onPositionChangedList.remove(listener)
    }

    private val _onTimeChanged: (MediaPlayer?, Long) -> Unit = { player, newTime ->
        onTimeChangedList.forEach {
            it.invoke(player, newTime)
        }
    }
    private var onTimeChangedList: MutableList<(MediaPlayer?, Long) -> Unit> = mutableListOf()

    fun addOnTimeChangedListener(listener: (MediaPlayer?, Long) -> Unit) {
        onTimeChangedList.add(listener)
    }

    fun removeOnTimeChangedListener(listener: (MediaPlayer?, Long) -> Unit) {
        onTimeChangedList.remove(listener)
    }

    private fun getMusicTitle(): String {
        runCatching {
            val rawTitle = player.media().meta()?.get(Meta.TITLE).orEmpty()
            val encodedTitle = String(rawTitle.toByteArray(Charset.forName("ASCII")), Charsets.UTF_8)
            println(rawTitle.toByteArray(Charset.forName("ASCII")))
            return encodedTitle.ifBlank {
                val filePath = player.media().info()?.mrl()
                if (filePath.isNullOrBlank()) "" else File(filePath).name
            }
        }.onFailure {
            it.printStackTrace()
        }
        return ""
    }

    init {
        musicList = appDatabase.historyQueries.getAllHistory().executeAsList()
            .sortedBy { -it.playTimestamp }.map { MusicCover1Bean(path = it.path) }.toMutableList()

        if (musicList.isNotEmpty()) {
            scope.launch { prepare(musicList[0].path) }
        }

        player.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
            override fun positionChanged(mediaPlayer: MediaPlayer?, newPosition: Float) {
                _onPositionChanged.invoke(mediaPlayer, newPosition)
            }

            override fun timeChanged(mediaPlayer: MediaPlayer?, newTime: Long) {
                _onTimeChanged.invoke(mediaPlayer, newTime)
            }

            override fun finished(mediaPlayer: MediaPlayer?) {
                when (switchMusicProxy) {
                    SwitchMusicProxy.Once -> {}
                    SwitchMusicProxy.RepeatOne -> scope.launch {
                        prepare(musicList[musicIndex].path)
                        play()
                    }
                    SwitchMusicProxy.Next -> next()
                    SwitchMusicProxy.ListLoop -> nextOrFirst()
                }
            }
        })
        player.events().addMediaEventListener(object : MediaEventAdapter() {
            override fun mediaStateChanged(media: Media?, newState: State) {
                playState = newState
                if (newState == State.PLAYING) {
                    duration = player.media().info()?.duration() ?: 0L
                    metaData = player.media().meta()?.asMetaData()
                }
            }

            override fun mediaMetaChanged(media: Media?, metaType: Meta?) {
                metaData = player.media().meta()?.asMetaData()
            }
        })
    }
}