package io.familymoments.app.ui.component

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.familymoments.app.ui.theme.AppColors

fun Modifier.dropShadow(
    color: Color = AppColors.black1.copy(alpha = 0.25f),
    borderRadius: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 2.7317073345184326.dp,
    blurRadius: Dp = 5.463414669036865.dp,
    spreadRadius: Dp = 0.dp,
    modifier: Modifier = Modifier
) = then(
    modifier.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spreadRadius.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = size.width + spreadPixel
            val bottomPixel = size.height + spreadPixel

            frameworkPaint.color = color.toArgb()

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = BlurMaskFilter(
                    blurRadius.toPx(),
                    BlurMaskFilter.Blur.NORMAL
                )
            }

            canvas.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint = paint
            )
        }
    }
)

