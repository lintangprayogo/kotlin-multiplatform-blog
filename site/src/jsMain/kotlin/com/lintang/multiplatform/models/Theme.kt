package com.lintang.multiplatform.models

import com.varabyte.kobweb.compose.ui.graphics.Color
import org.jetbrains.compose.web.css.CSSColorValue

enum class Theme(val hex: String, var rgb: CSSColorValue) {
    Primary(
        hex = "#e2603c",
        rgb = Color.rgb(r=226, g=96, b=60)
    ),
    Secondary(
        hex = "#000000",
        rgb = Color.rgb(r = 0, g = 0, b = 0)
    ),
    Tertiary(
        hex = "#F4F4FB",
        rgb = Color.rgb(r = 244, g = 244, b = 251)
    ),
    LightGray(
        hex = "#FAFAFA",
        rgb = Color.rgb(r = 250, g = 250, b = 250)
    ),
    Gray(
        hex = "#E9E9E9",
        rgb = Color.rgb(r = 233, g = 233, b = 233)
    ),
    DarkGray(
        hex = "#646464",
        rgb = Color.rgb(r = 100, g = 100, b = 100)
    ),
    HalfWhite(
        hex = "#FFFFFF",
        rgb = Color.rgba(r = 255, g = 255, b = 255, a = 0.5f)
    ),
    HalfBlack(
        hex = "#000000",
        rgb = Color.rgba(r = 0, g = 0, b = 0, a = 0.5f)
    ),
    Black(
        hex = "#000000",
        rgb = Color.rgb(r = 0, g = 0, b = 0)
    ),
    White(
        hex = "#FFFFFF",
        rgb = Color.rgb(r = 255, g = 255, b = 255)
    ),
    Green(
        hex = "#00FF94",
        rgb = Color.rgb(r = 0, g = 255, b = 148)
    ),
    Yellow(
        hex = "#FFEC45",
        rgb = Color.rgb(r = 255, g = 236, b = 69)
    ),
    Red(
        hex = "#FF6359",
        rgb = Color.rgb(r = 255, g = 99, b = 89)
    ),
    Purple(
        hex = "#8B6DFF",
        rgb = Color.rgb(r = 139, g = 109, b = 255)
    ),
    Sponsored(
        hex = "#3300FF",
        rgb = Color.rgb(r = 51, g = 0, b = 255)
    )
}