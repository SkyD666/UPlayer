package ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class AnimeTopBarStyle {
    Small, Large
}

@Composable
fun UPlayerTopBar(
    modifier: Modifier = Modifier,
    style: AnimeTopBarStyle = AnimeTopBarStyle.Small,
    title: @Composable () -> Unit,
    contentPadding: @Composable () -> PaddingValues = { PaddingValues() },
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val colors = when (style) {
        AnimeTopBarStyle.Small -> TopAppBarDefaults.smallTopAppBarColors()
        AnimeTopBarStyle.Large -> TopAppBarDefaults.largeTopAppBarColors()
    }
    val scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    val appBarContainerColor by colors.containerColor(scrollFraction)
    val topBarModifier = Modifier.padding(contentPadding())
    Surface(modifier = modifier, color = appBarContainerColor) {
        when (style) {
            AnimeTopBarStyle.Small -> {
                SmallTopAppBar(
                    modifier = topBarModifier,
                    title = title,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
            AnimeTopBarStyle.Large -> {
                LargeTopAppBar(
                    modifier = topBarModifier,
                    title = title,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    colors = colors,
                    scrollBehavior = scrollBehavior
                )
            }
        }
    }
}

@Composable
fun TopBarIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    tint: Color = LocalContentColor.current,
    contentDescription: String?,
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = modifier.size(24.dp),
            painter = painter,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun TopBarIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    tint: Color = LocalContentColor.current,
    contentDescription: String?,
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = modifier.size(24.dp),
            imageVector = imageVector,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BackIcon(onClick: () -> Unit = {}) {
    TopBarIcon(
        imageVector = Icons.Rounded.ArrowBack,
        contentDescription = "返回",
        onClick = onClick
    )
}
