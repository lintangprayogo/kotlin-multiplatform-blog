package com.lintang.multiplatform.pages.admin.create_components

import androidx.compose.runtime.Composable
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants
import com.lintang.multiplatform.util.noBorder
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button

@Composable
fun CreateButton(onClick: () -> Unit, hasParams: Boolean) {
    Button(
        attrs = Modifier.onClick {
            onClick()
        }.fillMaxWidth()
            .height(54.px)
            .margin(top = 24.px)
            .backgroundColor(Theme.Primary.rgb)
            .color(Theme.White.rgb)
            .borderRadius(4.px)
            .noBorder()
            .fontFamily(Constants.FONT_FAMILY)
            .fontSize(16.px)
            .toAttrs()
    ) {
        SpanText(if (hasParams) "Edit" else "Create")
    }
}