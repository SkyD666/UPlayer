package ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun MessageDialog(
    title: String = "警告",
    message: String,
    icon: ImageVector? = null,
    negativeText: String = "取消",
    positiveText: String = "确定",
    onDismissRequest: (() -> Unit)? = null,
    onNegative: (() -> Unit)? = null,
    onPositive: (() -> Unit)? = null,
) {
    MessageDialog(
        title = title,
        message = message,
        icon = if (icon == null) null else rememberVectorPainter(icon),
        negativeText = negativeText,
        positiveText = positiveText,
        onDismissRequest = onDismissRequest,
        onNegative = onNegative,
        onPositive = onPositive
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessageDialog(
    title: String = "警告",
    message: String,
    icon: Painter? = null,
    negativeText: String = "取消",
    positiveText: String = "确定",
    onDismissRequest: (() -> Unit)? = null,
    onNegative: (() -> Unit)? = null,
    onPositive: (() -> Unit)? = null,
) {
    val dismissButton: @Composable (() -> Unit) = {
        TextButton(
            onClick = {
                onNegative?.invoke()
            }
        ) {
            Text(text = negativeText)
        }
    }
    val iconLambda: @Composable (() -> Unit)? = if (icon != null) {
        { Icon(painter = icon, contentDescription = null) }
    } else null
    AlertDialog(
//        icon = if (icon != null) iconLambda else null,
        title = {
            Text(text = title)
        },
        text = {
            Text(
                modifier = Modifier.widthIn(min = 250.dp),
                text = message
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositive?.invoke()
                }
            ) {
                Text(text = positiveText)
            }
        },
        dismissButton = if (onNegative == null) null else dismissButton,
        onDismissRequest = {
            onDismissRequest?.invoke()
        }
    )
}
