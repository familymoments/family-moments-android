package io.familymoments.app.feature.choosingfamily

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import io.familymoments.app.R

@Composable
fun MemberCheckBox(
    modifier: Modifier,
    initChecked: Boolean = false,
    onCheckChanged: (Boolean) -> Unit = {},
) {
    var checked by remember(key1 = initChecked) {
        mutableStateOf(initChecked)
    }
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier, contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.clickable(indication = null, interactionSource = interactionSource) {
                checked = !checked
                onCheckChanged(checked)
            },
            painter = painterResource(id = if (checked) R.drawable.ic_round_checked else R.drawable.ic_round_unchecked),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
