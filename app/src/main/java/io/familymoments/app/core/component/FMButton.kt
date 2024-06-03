package io.familymoments.app.core.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
    textModifier:Modifier = Modifier.padding(vertical = 6.dp),
    contentPaddingValues: PaddingValues = PaddingValues(vertical = 4.dp, horizontal = 18.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.deepPurple1,
            disabledContainerColor = AppColors.grey3
        ),
        shape = RoundedCornerShape(radius),
        contentPadding = contentPaddingValues
    ) {
        Text(
            text = text,
            modifier = textModifier,
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
