import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.main.MainScreen
import javax.swing.UIManager

fun main() = application {
    LaunchedEffect(Unit) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    }
    MaterialTheme {
        Window(onCloseRequest = ::exitApplication) {
            MainScreen()
        }
    }
}
