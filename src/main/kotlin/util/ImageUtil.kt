package util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.unit.Density
import org.xml.sax.InputSource
import java.io.File
import java.net.URI
import java.net.URL

object ImageUtil {
    fun loadImageBitmap(bytes: ByteArray): ImageBitmap =
        bytes.inputStream().buffered().use(::loadImageBitmap)

    fun loadImageBitmap(uri: URI): ImageBitmap =
        File(uri).inputStream().buffered().use(::loadImageBitmap)

    /* Loading from file with java.io API */
    fun loadImageBitmap(file: File): ImageBitmap =
        file.inputStream().buffered().use(::loadImageBitmap)

    fun loadSvgPainter(file: File, density: Density): Painter =
        file.inputStream().buffered().use { loadSvgPainter(it, density) }

    fun loadXmlImageVector(file: File, density: Density): ImageVector =
        file.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

    /* Loading from network with java.net API */
    fun loadImageBitmap(url: String): ImageBitmap =
        URL(url).openStream().buffered().use(::loadImageBitmap)

    fun loadSvgPainter(url: String, density: Density): Painter =
        URL(url).openStream().buffered().use { loadSvgPainter(it, density) }

    fun loadXmlImageVector(url: String, density: Density): ImageVector =
        URL(url).openStream().buffered().use { loadXmlImageVector(InputSource(it), density) }
}