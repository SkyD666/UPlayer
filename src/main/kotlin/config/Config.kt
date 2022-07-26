package config

import java.io.File

object Config {
    const val APP_NAME = "UPlayer"

    val currentPath = File("").absoluteFile.path
}