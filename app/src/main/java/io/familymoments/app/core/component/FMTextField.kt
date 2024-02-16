package io.familymoments.app.core.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography

@Composable
fun FMTextField(
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    value: TextFieldValue,
    hint: String,
    showBorder: Boolean = true
) {
    Box(
        modifier = modifier
            .then(
                if (showBorder) {
                    Modifier.border(width = 2.dp, color = AppColors.grey2, shape = RoundedCornerShape(8.dp))
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                onValueChange = onValueChange,
                value = value,
                singleLine = true,
                textStyle = AppTypography.BTN5_16,
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(text = hint, style = AppTypography.BTN5_16, color = AppColors.grey2)
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .weight(1f)
            )

            if (value.text.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onValueChange(TextFieldValue("")) }
                )
            }

        }
    }
}

@Preview
@Composable
fun FMTextFieldPreview() {
    FMTextField(
        onValueChange = {},
        value = TextFieldValue("asd"),
        hint = "Hint"
    )
}
