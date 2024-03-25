package io.familymoments.app.feature.choosingfamily

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography

@Composable
fun SearchTextField(
    hint: String,
    singleLine:Boolean = false,
    onValueChange: (TextFieldValue) -> Unit
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }
    Box(
        modifier = Modifier
            .padding(bottom = 14.dp)
            .border(width = 1.5.dp, color = AppColors.grey2, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = 7.5.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = AppColors.grey2
            )
            BasicTextField(
                onValueChange = {
                    textFieldValue = it
                    onValueChange(textFieldValue)
                },
                value = textFieldValue,
                textStyle = AppTypography.LB1_13.copy(color = AppColors.black1),
                decorationBox = { innerTextField ->
                    if (textFieldValue.text.isEmpty()) {
                        Text(
                            text = hint,
                            style = AppTypography.LB1_13,
                            color = AppColors.grey2
                        )
                    }
                    innerTextField()
                },
                singleLine = singleLine
            )
        }

    }
}

@Preview
@Composable
fun SearchTextFieldPreview() {
    SearchTextField(hint = "", onValueChange = {})
}
