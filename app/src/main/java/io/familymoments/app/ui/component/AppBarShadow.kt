package io.familymoments.app.ui.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.familymoments.app.ui.theme.AppColors

fun Modifier.appBarShadow(
    color: Color = AppColors.shadowColor,
    alpha: Float = 0.15f,
    shadowRadius: Dp = 5.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
) = this.drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRect(
            left = 0f,
            top = 0f,
            right = this.size.width,
            bottom = this.size.height + 5.dp.toPx(),
            paint = paint
        )
    }
}
