package player

import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.media.TrackType
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener

open class MediaPlayerEventListenerImpl : MediaPlayerEventListener {
    override fun forward(mediaPlayer: MediaPlayer?) {}

    override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {}

    override fun opening(mediaPlayer: MediaPlayer?) {}

    override fun buffering(mediaPlayer: MediaPlayer?, newCache: Float) {}

    override fun playing(mediaPlayer: MediaPlayer?) {}

    override fun paused(mediaPlayer: MediaPlayer?) {}

    override fun stopped(mediaPlayer: MediaPlayer?) {}

    override fun backward(mediaPlayer: MediaPlayer?) {}

    override fun finished(mediaPlayer: MediaPlayer?) {}

    override fun timeChanged(mediaPlayer: MediaPlayer?, newTime: Long) {}

    override fun positionChanged(mediaPlayer: MediaPlayer?, newPosition: Float) {}

    override fun seekableChanged(mediaPlayer: MediaPlayer?, newSeekable: Int) {}

    override fun pausableChanged(mediaPlayer: MediaPlayer?, newPausable: Int) {}

    override fun titleChanged(mediaPlayer: MediaPlayer?, newTitle: Int) {}

    override fun snapshotTaken(mediaPlayer: MediaPlayer?, filename: String?) {}

    override fun lengthChanged(mediaPlayer: MediaPlayer?, newLength: Long) {}

    override fun videoOutput(mediaPlayer: MediaPlayer?, newCount: Int) {}

    override fun scrambledChanged(mediaPlayer: MediaPlayer?, newScrambled: Int) {}

    override fun elementaryStreamAdded(mediaPlayer: MediaPlayer?, type: TrackType?, id: Int) {}

    override fun elementaryStreamDeleted(mediaPlayer: MediaPlayer?, type: TrackType?, id: Int) {}

    override fun elementaryStreamSelected(mediaPlayer: MediaPlayer?, type: TrackType?, id: Int) {}

    override fun corked(mediaPlayer: MediaPlayer?, corked: Boolean) {}

    override fun muted(mediaPlayer: MediaPlayer?, muted: Boolean) {}

    override fun volumeChanged(mediaPlayer: MediaPlayer?, volume: Float) {}

    override fun audioDeviceChanged(mediaPlayer: MediaPlayer?, audioDevice: String?) {}

    override fun chapterChanged(mediaPlayer: MediaPlayer?, newChapter: Int) {}

    override fun error(mediaPlayer: MediaPlayer?) {}

    override fun mediaPlayerReady(mediaPlayer: MediaPlayer?) {}
}