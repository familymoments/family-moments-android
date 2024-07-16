package io.familymoments.app.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography


@Composable
fun PostDropdownMenu2(
    modifier: Modifier = Modifier,
    items: List<Pair<String, () -> Unit>>,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.then(
            Modifier
                .width(112.dp)
                .background(color = AppColors.grey7, shape = RoundedCornerShape(5.dp))
        )
    )
    {
        items.forEach {
            DropdownMenuItem(
                text = {
                    Text(
                        text = it.first,
                        style = AppTypography.LB2_11,
                        color = AppColors.grey6
                    )
                },
                onClick = it.second,
                modifier = Modifier.height(35.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDropdownMenu2Preview() {
    PostDropdownMenu2(
        items = listOf(Pair("아이템1") {}, Pair("아이템2") {}),
        expanded = true,
        onDismissRequest = {},
    )
}
