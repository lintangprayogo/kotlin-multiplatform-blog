package com.lintang.multiplatform.section

import androidx.compose.runtime.Composable
import com.lintang.multiplatform.components.SearchBar
import com.lintang.multiplatform.models.Category
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.style.CategoryItemStyle
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Constants.HEADER_HEIGHT
import com.lintang.multiplatform.util.Constants.PAGE_WIDTH
import com.lintang.multiplatform.util.Res
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun HeaderSection(breakpoint: Breakpoint) {
    Box(modifier = Modifier.fillMaxWidth().background(Theme.Secondary.rgb)) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Theme.Secondary.rgb)
                .maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.Center
        ) {
            Header(breakpoint)
        }
    }
}

@Composable
fun Header(breakpoint: Breakpoint) {
    Row(
        modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .height(
                HEADER_HEIGHT.px
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.margin(right = 50.px)
                .width(if (breakpoint >= Breakpoint.SM) 100.px else 70.px)
                .cursor(Cursor.Pointer)
                .onClick {

                },
            src = Res.Image.logo
        )

        if (breakpoint >= Breakpoint.LG) {
            Category.entries.forEach {
                Link(
                    modifier = Modifier
                        .then(CategoryItemStyle.toModifier())
                        .margin(right = 24.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .fontWeight(FontWeight.Medium)
                        .textDecorationLine(TextDecorationLine.None)
                        .onClick {},
                    text = it.name,
                    path = ""
                )
            }
        }
        Spacer()
        SearchBar(onEnterClick = {

        })
    }
}