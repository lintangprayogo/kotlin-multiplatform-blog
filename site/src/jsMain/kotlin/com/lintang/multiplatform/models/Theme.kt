package com.lintang.multiplatform.models

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.rgba

enum class Theme(val hex: String, var rgb: CSSColorValue) {
    Primary(
        hex = "#00A2FF",
        rgb = rgb(r = 0, g = 162, b = 255)
    ),
    Secondary(hex = "#001019", rgb(r = 0, g = 16, b = 25)),
    HalfWhite(hex = "#FFFFFF", rgba(r = 255, g = 255, b = 255, a = 0.5)),
    White(hex = "#FFFFFF", rgb(r = 255, g = 255, b = 255)),
    HalfBlack(hex = "#000000", rgba(r = 0, g = 0, b = 0, a = 0.5)),
    Black(hex = "#000000", rgb(r = 0, g = 0, b = 0)),
    LightGrey(
        hex = "#FAFAFA",
        rgb = rgb(r = 250, g = 250, b = 250)
    )
}