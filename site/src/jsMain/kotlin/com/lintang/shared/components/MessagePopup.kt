package com.lintang.shared.components

import androidx.compose.runtime.Composable
import com.lintang.shared.models.EditorControl
import com.lintang.shared.models.Theme
import com.lintang.shared.util.Constants
import com.lintang.shared.util.Id
import com.lintang.shared.util.noBorder
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement

@Composable
fun MessagePopup(
    message: String,
    onMessageDismiss: () -> Unit
) {
    Box(
        modifier = Modifier.width(100.vw)
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(19),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(Theme.HalfBlack.rgb)
                .onClick { onMessageDismiss() }
        )

        Box(
            modifier = Modifier.padding(all = 24.px)
                .backgroundColor(Colors.White)
                .borderRadius(4.px)
        ) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .textAlign(TextAlign.Center)
                    .fontFamily(Constants.FONT_FAMILY).fontSize(16.px),
                text = message
            )
        }
    }

}

@Composable
fun LinkPopup(
    editorControl: EditorControl,
    onLinkDismiss: () -> Unit,
    onAddClick: (String, String) -> Unit,
) {
    Box(
        modifier = Modifier.width(100.vw)
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(19),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(Theme.HalfBlack.rgb)
                .onClick { onLinkDismiss() }
        )

        Column(
            modifier = Modifier
                .width(500.px)
                .padding(all = 24.px)
                .backgroundColor(Colors.White)
                .borderRadius(4.px)
        ) {
            Input(
                attrs = Modifier.fillMaxWidth()
                    .id(Id.linkTitleInput)
                    .height(54.px)
                    .margin(bottom = 12.px)
                    .padding(leftRight = 24.px)
                    .background(Theme.LightGray.rgb)
                    .borderRadius(4.px)
                    .noBorder()
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(16.px)
                    .toAttrs {
                        attr("placeholder",  if(editorControl == EditorControl.Link) "Title" else "Description")
                    },
                type = InputType.Text,
            )

            Input(
                attrs = Modifier.fillMaxWidth()
                    .id(Id.linkHrefInput)
                    .height(54.px)
                    .margin(bottom = 20.px)
                    .padding(leftRight = 24.px)
                    .background(Theme.LightGray.rgb)
                    .borderRadius(4.px)
                    .noBorder()
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(16.px)
                    .toAttrs {
                        attr("placeholder", if(editorControl == EditorControl.Link)"Href" else "Image Url")
                    },
                type = InputType.Text,
            )

            Button(
                attrs = Modifier.fillMaxWidth()
                    .onClick {
                        val  href = (document.getElementById(Id.linkHrefInput) as HTMLInputElement).value
                        val  title = (document.getElementById(Id.titleInput) as HTMLInputElement).value
                        onAddClick(title,href)
                    }
                    .height(54.px)
                    .margin(bottom = 12.px)
                    .padding(leftRight = 24.px)
                    .background(Theme.Primary.rgb)
                    .color(Colors.White)
                    .borderRadius(4.px)
                    .noBorder()
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(16.px).toAttrs()
            ) {
                SpanText("Input")
            }
        }
    }

}