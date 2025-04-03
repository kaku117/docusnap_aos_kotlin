package kr.co.docusnap.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kr.co.docusnap.presentation.R

private val paperlogyThin = FontFamily(
    Font(R.font.paperlogy_thin, FontWeight.Thin)
)

private val paperlogyLight = FontFamily(
    Font(R.font.paperlogy_light, FontWeight.Light)
)

private val paperlogyRegular = FontFamily(
    Font(R.font.paperlogy_regular, FontWeight.Normal)
)

private val paperlogyMedium = FontFamily(
    Font(R.font.paperlogy_medium, FontWeight.Medium)
)

private val paperlogySemiBold = FontFamily(
    Font(R.font.paperlogy_semi_bold, FontWeight.SemiBold)
)

private val paperlogyBold = FontFamily(
    Font(R.font.paperlogy_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = paperlogyRegular,
        fontSize = 18.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)