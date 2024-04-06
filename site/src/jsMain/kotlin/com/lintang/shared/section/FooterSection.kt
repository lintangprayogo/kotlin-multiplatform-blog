package com.lintang.shared.section

import androidx.compose.runtime.Composable
import com.lintang.shared.models.Theme
import com.lintang.shared.util.Constants.FONT_FAMILY
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px

@Composable
fun FooterSection() {
    Box(
        Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Black.rgb)
            .padding(topBottom = 50.px),
        contentAlignment = Alignment.Center
    ) {
        Row {
            SpanText(
                modifier = Modifier
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .color(Theme.White.rgb),
                text = "Copyright © 2024 . "
            )
            SpanText(
                modifier = Modifier
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .color(Theme.Primary.rgb),
                text = "Lintang Prayogo "
            )

        }

    }
}