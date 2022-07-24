import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.controller.MusicController


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                }
                MusicController()
            }
        }
    }
}
