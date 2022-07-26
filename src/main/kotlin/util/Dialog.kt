package util

import config.Config.currentPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Frame
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

@Suppress("UsePropertyAccessSyntax")
fun openFileDialog(parent: Frame? = null, filters: List<FileFilter>? = null): String? {
    val scope = CoroutineScope(Dispatchers.IO)
    val jFileChooser = JFileChooser(currentPath)
    if (!filters.isNullOrEmpty()) {
        scope.launch(Dispatchers.IO) {
            jFileChooser.removeChoosableFileFilter(jFileChooser.acceptAllFileFilter)
            filters.forEach { jFileChooser.addChoosableFileFilter(it) }
        }
    }
    val returnVal: Int = jFileChooser.showOpenDialog(parent)
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        return jFileChooser.getSelectedFile().path
    }
    return null
}

class OpenFileFilter : FileFilter {
    var typeDescription = ""
    var fileExt: List<String>

    constructor(extension: String) {
        fileExt = extension.split(";")
    }

    constructor(extension: String, typeDescription: String) {
        fileExt = extension.split(";")
        this.typeDescription = typeDescription
    }

    override fun accept(f: File?): Boolean {
        f ?: return false
        return if (f.isDirectory) true else {
            f.name.lowercase(Locale.getDefault()).substringAfterLast(".") in fileExt
        }
    }

    override fun getDescription(): String = typeDescription
}