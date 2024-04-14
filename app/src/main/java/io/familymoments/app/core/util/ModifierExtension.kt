package io.familymoments.app.core.util

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    this.clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}


inline fun Modifier.oneClick(crossinline onClick: () -> Unit): Modifier = composed {
    val buttonState = remember { mutableStateOf(true) }
    this.clickable {
        if (buttonState.value) {
            buttonState.value = false
            onClick()
        }
    }
}

inline fun Modifier.oneClick(delay: Long, crossinline onClick: () -> Unit): Modifier = composed {
    val buttonState = remember { mutableStateOf(true) }
    this.clickable {
        if (buttonState.value) {
            buttonState.value = false
            onClick()
            Handler(Looper.getMainLooper()).postDelayed({
                buttonState.value = true
            }, delay)
        }
    }
}
