package com.lintang.shared.pages.admin.create_components

import androidx.compose.runtime.Composable
import com.lintang.shared.models.Theme
import com.lintang.shared.util.Constants
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import com.lintang.shared.Category
@Composable
fun CategoryDropdown(
    selectedCategory: Category,
    onCategorySelect: (Category) -> Unit
) {

    Box(
        modifier = Modifier
            .classNames("dropdown")
            .margin(topBottom = 12.px)
            .fillMaxWidth()
            .height(54.px)
            .background(Theme.LightGray.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle", "dropdown")
            }) {

        Row(
            modifier = Modifier.fillMaxSize().margin(leftRight = 20.px).height(54.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .fontSize(16.px).fontFamily(Constants.FONT_FAMILY),
                text = selectedCategory.name
            )
            Box(modifier = Modifier.classNames("dropdown-toggle"))
        }

        Ul(
            attrs = Modifier
                .fillMaxWidth()
                .classNames("dropdown-menu")
                .toAttrs()
        ) {
            Category.entries.forEach { category ->
                Li {
                    A(
                        attrs = Modifier
                            .fillMaxWidth()
                            .classNames("dropdown-item")
                            .color(Colors.Black)
                            .fontSize(16.px)
                            .fontFamily(Constants.FONT_FAMILY)
                            .onClick { onCategorySelect(category) }
                            .toAttrs()
                    ) {
                        Text(category.name)
                    }

                }
            }
        }
    }
}