package io.familymoments.app.core.util

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.oneClick(onClick: () -> Unit): Modifier {
    val buttonState = remember { mutableStateOf(true) }
    return this.clickable {
        if (buttonState.value) {
            buttonState.value = false
            onClick()
        }
    }
}

@Composable
fun Modifier.oneClick(delay: Long, onClick: () -> Unit): Modifier {
    val buttonState = remember { mutableStateOf(true) }
    return this.clickable {
        if (buttonState.value) {
            buttonState.value = false
            onClick()
            Handler(Looper.getMainLooper()).postDelayed({
                buttonState.value = true
            }, delay)
        }
    }
}

@Composable
fun Modifier.noEffectClick(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        onClick()
    }
}
