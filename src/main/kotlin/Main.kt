import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import config.Config
import player.Player
import ui.main.MainScreen
import javax.swing.UIManager

fun main() {
    System.setProperty("compose.application.configure.swing.globals", "true")
    application {
        LaunchedEffect(Unit) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        }
        val appIcon = useResource("image/icon.svg") {
            loadSvgPainter(inputStream = it, density = LocalDensity.current)
        }
        var windowVisible by remember { mutableStateOf(true) }
        MaterialTheme {
            val trayState = rememberTrayState()
            val windowState = rememberWindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                size = DpSize(1000.dp, 700.dp)
            )

            Tray(
                state = trayState,
                icon = appIcon,
                onAction = { windowVisible = true },
                menu = {
                    Item(
                        text = "显示",
                        onClick = { windowVisible = true }
                    )
                    Item(
                        text = "退出",
                        onClick = ::exitApplication
                    )
                }
            )

            Window(
                state = windowState,
                title = Config.APP_NAME,
                icon = appIcon,
                visible = windowVisible,
                onCloseRequest = { windowVisible = false }
            ) {
                MainScreen()
            }
        }
    }

    Player.release()
}