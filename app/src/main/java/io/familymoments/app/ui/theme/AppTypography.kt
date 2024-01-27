package io.familymoments.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.familymoments.app.R

object AppTypography {

    val typography = Typography()

    private val roboto = FontFamily(
        Font(R.font.roboto_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.roboto_medium, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.roboto_regular, FontWeight.Normal, FontStyle.Normal)
    )

    val H1_36 = TextStyle(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
    )

    val H2_24 = TextStyle(
        fontSize = 24.sp,
        lineHeight = 29.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
    )

    val SH1_20 = TextStyle(
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
    )

    val SH2_18 = TextStyle(
        fontSize = 18.sp,
        lineHeight = 21.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Medium,
    )

    val SH3_16 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 19.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
    )

    val B1_16 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 19.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Medium,
    )

    val B2_14 = TextStyle(
        fontSize = 14.sp,
        lineHeight = 16.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
    )

    val BTN1_36 = TextStyle(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        fontFamily = FontFamily(Font(R.font.noto_sans_kr_bold)),
        fontWeight = FontWeight.Bold,
    )

    val BTN2_30 = TextStyle(
        fontSize = 30.sp,
        lineHeight = 34.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.SemiBold,
    )

    val BTN3_20 = TextStyle(
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.SemiBold,
    )

    val BTN4_18 = TextStyle(
        fontSize = 18.sp,
        lineHeight = 21.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
    )

    val BTN5_16 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
    )

    val BTN6_13 = TextStyle(
        fontSize = 13.sp,
        lineHeight = 16.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
    )

    val LB1_13 = TextStyle(
        fontSize = 13.sp,
        lineHeight = 16.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
    )

    val LB2_11 = TextStyle(
        fontSize = 11.sp,
        lineHeight = 13.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
    )

    val LB3_9 = TextStyle(
        fontSize = 9.sp,
        lineHeight = 13.sp,
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
    )
}

