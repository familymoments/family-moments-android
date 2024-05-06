package io.familymoments.app.core.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.FMVisualTransformation

@Composable
fun FMTextField(
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    value: TextFieldValue,
    hint: String,
    showBorder: Boolean = true,
    borderColor: Color = AppColors.grey2,
    showDeleteButton: Boolean = true,
    textColor: Color = AppColors.black1,
    hintColor: Color = AppColors.grey2,
    showText: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions {}
) {
    Box(
        modifier = modifier
            .then(
                if (showBorder) {
                    Modifier.border(width = 1.5.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                } else {
                    Modifier
                }
            )
            .height(46.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                onValueChange = onValueChange,
                value = value,
                singleLine = true,
                textStyle = AppTypography.BTN5_16.copy(color = textColor),
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(text = hint, style = AppTypography.BTN5_16, color = hintColor)
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f)
                    .onFocusChanged {
                        onFocusChanged(it.isFocused)
                    },
                visualTransformation = if (showText) VisualTransformation.None else FMVisualTransformation(),
                keyboardOptions = if (showText) KeyboardOptions.Default else KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = keyboardActions
            )

            if (showDeleteButton && value.text.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_text_field_delete),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(28.dp)
                        .clickable { onValueChange(TextFieldValue("")) }
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FMTextFieldPreview() {
    FMTextField(
        onValueChange = {},
        value = TextFieldValue("asd"),
        hint = "Hint"
    )
}
