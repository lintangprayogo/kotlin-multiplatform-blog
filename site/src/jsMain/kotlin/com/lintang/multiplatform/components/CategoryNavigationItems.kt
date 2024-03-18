package com.lintang.multiplatform.components

import androidx.compose.runtime.Composable
import com.lintang.multiplatform.models.Category
import com.lintang.multiplatform.style.CategoryItemStyle
import com.lintang.multiplatform.util.Constants
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px

@Composable

fun CategoryNavigationItems(isVertical: Boolean) {
    Category.entries.forEach {
        Link(
            modifier = CategoryItemStyle.toModifier()
                .thenIf(condition = isVertical, other = Modifier.margin(bottom = 24.px))
                .thenIf(condition = !isVertical, other = Modifier.margin(right = 24.px))
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(16.px)
                .fontWeight(FontWeight.Medium)
                .textDecorationLine(TextDecorationLine.None)
                .onClick {},
            text = it.name,
            path = ""
        )
    }
}