package io.familymoments.app.core.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography

@Composable
fun FMButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    radius: Dp = 60.dp,
    textStyle: TextStyle = AppTypography.BTN4_18,
    contentPaddingValues: PaddingValues = PaddingValues(vertical = 10.dp, horizontal = 18.dp),
    containerColor:Color = AppColors.grey8
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = AppColors.grey3
        ),
        shape = RoundedCornerShape(radius),
        contentPadding = contentPaddingValues
    ) {
        Text(
            text = text,
            style = textStyle,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun FMButtonPreview() {
    FMButton(onClick = {}, text = "Button")
}
