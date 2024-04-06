package com.lintang.shared.pages.admin.create_components

import androidx.compose.runtime.Composable
import com.lintang.shared.models.ControlStyle
import com.lintang.shared.models.EditorControl
import com.lintang.shared.models.Theme
import com.lintang.shared.style.EditorKeyStyle
import com.lintang.shared.util.Constants.FONT_FAMILY
import com.lintang.shared.util.Id
import com.lintang.shared.util.applyControlStyle
import com.lintang.shared.util.applyStyle
import com.lintang.shared.util.getEditor
import com.lintang.shared.util.getSelectedText
import com.lintang.shared.util.noBorder
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.TextArea

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorsVisibile: Boolean,
    onEditorVisibilityChange: (Boolean) -> Unit,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SimpleGrid(numColumns = numColumns(base = 1, sm = 2)) {
            Row(
                modifier = Modifier
                    .backgroundColor(Theme.LightGray.rgb)
                    .height(54.px)
            ) {
                EditorControl.entries.forEach { key ->
                    EditorKeyView(key = key,
                        onClick = {
                            applyControlStyle(it, onLinkClick = onLinkClick,onImageClick=onImageClick)
                        })
                }
            }
            Box(contentAlignment = Alignment.CenterEnd) {
                Button(
                    attrs = Modifier.height(54.px)
                        .thenIf(
                            condition = breakpoint < Breakpoint.SM,
                            other = Modifier.fillMaxWidth()
                        )
                        .margin(topBottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px)
                        .padding(leftRight = 24.px)
                        .borderRadius(r = 4.px)
                        .backgroundColor(
                            if (editorsVisibile) Theme.LightGray.rgb
                            else Theme.Primary.rgb
                        )
                        .color(
                            if (editorsVisibile) Theme.DarkGray.rgb
                            else Colors.White
                        )
                        .noBorder()
                        .onClick {
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            onEditorVisibilityChange(!editorsVisibile)
                            try {
                                js("hljs.highlightAll()") as Unit
                            }catch (e:Exception){
                                println("error hightlight ${e.message}")
                            }
                        }
                        .toAttrs()
                ) {
                    SpanText(text = "Preview")
                }
            }


        }
    }

}

@Composable
fun EditorKeyView(key: EditorControl, onClick: (key: EditorControl) -> Unit) {
    Box(
        modifier = Modifier
                then (EditorKeyStyle.toModifier())
            .fillMaxHeight()
            .padding(leftRight = 12.px)
            .borderRadius(r = 4.px)
            .cursor(Cursor.Pointer)
            .onClick {
                onClick(key)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(src = key.icon, description = "${key.name} Icon")
    }

}

@Composable
fun Editors(editorsVisibile: Boolean) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextArea(
            attrs = Modifier
                .id(Id.editor)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .resize(Resize.None)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .border(4.px)
                .noBorder()
                .visibility(if (editorsVisibile) Visibility.Visible else Visibility.Hidden)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .onKeyDown {
                    //AGAR DAPAT MENAMBAHKAN BREAK PADA TEXT
                    if(it.code =="Enter" && it.shiftKey){
                        applyStyle(ControlStyle.Break(selectedText = getSelectedText()))
                    }
                }
                .toAttrs {
                    attr("placeholder", "Type here...")
                },
        )

        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .resize(Resize.None)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .border(4.px)
                .noBorder()
                .visibility(if (!editorsVisibile) Visibility.Visible else Visibility.Hidden)
                .toAttrs()
        ) { }

    }
}