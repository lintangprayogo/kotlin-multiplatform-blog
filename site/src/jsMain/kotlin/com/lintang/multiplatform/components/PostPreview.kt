package com.lintang.multiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.models.PostWithoutDetails
import com.lintang.multiplatform.style.MainPostPreviewStyle
import com.lintang.multiplatform.style.PostPreviewStyle
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.parseDateString
import com.lintang.shared.JsTheme
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    modifier: Modifier = Modifier,
    post: PostWithoutDetails,
    selectableMode: Boolean = false,
    darkTheme: Boolean = false,
    isVertical: Boolean = true,
    thumbnailHeight: CSSSizeValue<CSSUnit.px> = 320.px,
    titleMaxLines: Int = 2,
    titleColor: CSSColorValue = Colors.Black,
    onSelect: (id: String) -> Unit = {},
    onDeselect: (id: String) -> Unit = {},
    onDetail: (id: String) -> Unit = {},
) {
    var checked by remember(selectableMode) { mutableStateOf(false) }
    if (!isVertical) {
        Row(
            modifier = Modifier.thenIf(
                condition = post.isMain,
                other = MainPostPreviewStyle.toModifier()
            )
                .thenIf(
                    condition = !post.isMain,
                    other = PostPreviewStyle.toModifier()
                )
                .then(modifier)
                .height(thumbnailHeight)
                .onClick { onDetail(post._id) }
                .cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                thumbnailHeight = thumbnailHeight,
                titleMaxLines = titleMaxLines,
                titleColor = titleColor,
                vertical = isVertical,
            )
        }
    } else {
        Column(
            modifier = Modifier
                .thenIf(
                    condition = post.isMain,
                    other = MainPostPreviewStyle.toModifier()
                )
                .thenIf(
                    condition = !post.isMain,
                    other = PostPreviewStyle.toModifier()
                )
                .then(modifier)
                .fillMaxWidth(
                    if (darkTheme) 100.percent
                    else if (titleColor == JsTheme.Sponsored.rgb) 100.percent
                    else 95.percent
                )
                .margin(bottom = 24.px)
                .padding(all = if (selectableMode) 10.px else 0.px)
                .borderRadius(r = 4.px)
                .border(
                    width = if (selectableMode) 4.px else 0.px,
                    style = if (selectableMode) LineStyle.Solid else LineStyle.None,
                    color = if (checked) JsTheme.Primary.rgb else JsTheme.Gray.rgb
                )
                .onClick {
                    if (selectableMode) {
                        checked = !checked
                        if (checked) {
                            onSelect(post._id)
                        } else {
                            onDeselect(post._id)
                        }
                    } else {
                        onDetail(post._id)
                    }
                }
                .transition(CSSTransition(property = TransitionProperty.All, duration = 200.ms))
                .cursor(Cursor.Pointer)
        ) {

            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                thumbnailHeight = thumbnailHeight,
                titleMaxLines = titleMaxLines,
                vertical = isVertical,
                titleColor = titleColor,
            )
        }
    }


}

@Composable
fun PostContent(
    post: PostWithoutDetails,
    darkTheme: Boolean,
    selectableMode: Boolean,
    checked: Boolean,
    thumbnailHeight: CSSSizeValue<CSSUnit.px> = 320.px,
    titleMaxLines: Int = 2,
    titleColor: CSSColorValue,
    vertical: Boolean,
) {
    Image(
        modifier = Modifier
            .margin(bottom = if (darkTheme) 20.px else 16.px)
            .height(size = thumbnailHeight)
            .fillMaxWidth()
            .objectFit(ObjectFit.Cover),
        src = post.thumbnail,
        alt = "Post Thumbnail Image"
    )
    Column(
        modifier = Modifier
            .thenIf(
                condition = !vertical,
                other = Modifier.margin(left = 20.px)
            )
            .padding(all = 12.px)
            .fillMaxWidth()
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(12.px)
                .color(if (darkTheme) JsTheme.HalfWhite.rgb else JsTheme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 12.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(20.px)
                .fontWeight(FontWeight.Bold)
                .color(if (darkTheme) Colors.White else titleColor)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "$titleMaxLines")
                    property("line-clamp", "$titleMaxLines")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.title
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 8.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .color(if (darkTheme) Colors.White else Colors.Black)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "3")
                    property("line-clamp", "3")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.subtitle
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryChip(category = post.category, darkTheme = darkTheme)
            if (selectableMode) {
                CheckboxInput(
                    checked = checked,
                    attrs = Modifier
                        .size(20.px)
                        .toAttrs()
                )
            }
        }
    }

}

