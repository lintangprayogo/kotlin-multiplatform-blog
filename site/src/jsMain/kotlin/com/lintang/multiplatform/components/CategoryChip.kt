package com.lintang.multiplatform.components

import androidx.compose.runtime.Composable
import com.lintang.multiplatform.models.Category
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun CategoryChip(category: Category, darkTheme: Boolean) {
    Box(
        modifier = Modifier
            .height(32.px)
            .padding(leftRight = 14.px)
            .borderRadius(100.px)
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = if (darkTheme) Theme.entries.find { it.hex == category.color }?.rgb
                    ?: Theme.HalfWhite.rgb else Theme.HalfBlack.rgb
            ),
        contentAlignment = Alignment.Center
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(12.px)
                .color(
                    if (darkTheme) Theme.entries.find { it.hex == category.color }?.rgb
                        ?: Theme.HalfWhite.rgb
                    else Theme.HalfBlack.rgb
                ),
            text = category.name
        )


    }
}