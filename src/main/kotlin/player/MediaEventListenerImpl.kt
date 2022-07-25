package player

import uk.co.caprica.vlcj.media.*
import uk.co.caprica.vlcj.player.base.State

open class MediaEventListenerImpl : MediaEventListener {
    override fun mediaMetaChanged(media: Media?, metaType: Meta?) {}

    override fun mediaSubItemAdded(media: Media?, newChild: MediaRef?) {}

    override fun mediaDurationChanged(media: Media?, newDuration: Long) {}

    override fun mediaParsedChanged(media: Media?, newStatus: MediaParsedStatus?) {}

    override fun mediaFreed(media: Media?, mediaFreed: MediaRef?) {}

    override fun mediaStateChanged(media: Media?, newState: State) {}

    override fun mediaSubItemTreeAdded(media: Media?, item: MediaRef?) {}

    override fun mediaThumbnailGenerated(media: Media?, picture: Picture?) {}
}