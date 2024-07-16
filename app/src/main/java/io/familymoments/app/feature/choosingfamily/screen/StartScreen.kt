package io.familymoments.app.feature.choosingfamily.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun StartScreen(goToCreating: () -> Unit = {}, goToJoining: () -> Unit = {}) {
    DrawCircle(goToCreating = goToCreating, goToJoining = goToJoining)
    CreatingText()
    JoiningText()
}


@Composable
private fun CreatingText() {
    Text(
        modifier = Modifier
            .padding(top = 146.dp, start = 43.dp),
        text = stringResource(id = R.string.create_family),
        style = AppTypography.BTN1_36,
        color = AppColors.grey8
    )
}

@Composable
private fun JoiningText() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Text(
            modifier = Modifier
                .padding(bottom = 227.dp, end = 50.dp),
            text = stringResource(id = R.string.join_family),
            style = AppTypography.BTN1_36,
            color = AppColors.grey8
        )
    }
}


private fun calculateCreateCircleCenter(radius: Int): Pair<Int, Int> {
    return (radius - 226 to 27 + radius)
}

private fun calculateJoinCircleCenter(radius: Int): Pair<Int, Int> {
    return (50 + radius to 234 + radius)
}

@Composable
private fun DrawCircle(goToCreating: () -> Unit, goToJoining: () -> Unit, radius: Int = 275) {
    var creatingCircleColor by remember {
        mutableStateOf(AppColors.pink3)
    }
    var joiningCircleColor by remember {
        mutableStateOf(AppColors.purple4)
    }

    val creatingCircleCenter = calculateCreateCircleCenter(radius)
    val creatingCircleCenterX = creatingCircleCenter.first.dp
    val creatingCircleCenterY = creatingCircleCenter.second.dp

    val joiningCircleCenter = calculateJoinCircleCenter(radius)
    val joiningCircleCenterX = joiningCircleCenter.first.dp
    val joiningCircleCenterY = joiningCircleCenter.second.dp

    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(goToCreating, goToJoining) {
            awaitEachGesture {
                awaitFirstDown().also {
                    val position = (it.position.x to it.position.y)
                    if (position.isInCircle(
                            joiningCircleCenterX.toPx(),
                            joiningCircleCenterY.toPx(),
                            radius.dp.toPx()
                        )
                    ) {
                        joiningCircleColor = AppColors.purple3
                        it.consume()
                    } else if (position.isInCircle(
                            creatingCircleCenterX.toPx(),
                            creatingCircleCenterY.toPx(),
                            radius.dp.toPx()
                        )
                    ) {
                        creatingCircleColor = AppColors.pink2
                        it.consume()
                    }
                }
                val up = waitForUpOrCancellation()
                if (up != null) {
                    val position = (up.position.x to up.position.y)
                    if (position.isInCircle(
                            joiningCircleCenterX.toPx(),
                            joiningCircleCenterY.toPx(),
                            radius.dp.toPx()
                        )
                    ) {
                        joiningCircleColor = AppColors.purple4
                        goToJoining()
                    } else if (position.isInCircle(
                            creatingCircleCenterX.toPx(),
                            creatingCircleCenterY.toPx(),
                            radius.dp.toPx()
                        )
                    ) {
                        creatingCircleColor = AppColors.pink3
                        goToCreating()
                    } else {
                        joiningCircleColor = AppColors.purple4
                        creatingCircleColor = AppColors.pink3
                    }
                    up.consume()
                }
            }
        }) {
        drawEachCircle(radius, creatingCircleColor, creatingCircleCenter)()
        drawEachCircle(radius, joiningCircleColor, joiningCircleCenter)()
    }
}

private fun Pair<Float, Float>.isInCircle(circleCenterX: Float, circleCenterY: Float, radius: Float): Boolean {
    return (sqrt(
        (first - circleCenterX).pow(2)
            + (second - circleCenterY).pow(2)
    ) <= radius
        )
}

private fun drawEachCircle(radius: Int, color: Color, circleCenter: Pair<Int, Int>): DrawScope.() -> Unit {
    return {
        drawCircle(
            color = color,
            radius = radius.dp.toPx(),
            center = Offset(circleCenter.first.dp.toPx(), circleCenter.second.dp.toPx())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStartScreen() {
    FamilyMomentsTheme {
        StartScreen()
    }
}
