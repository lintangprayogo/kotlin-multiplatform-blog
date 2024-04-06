package com.lintang.shared.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lintang.shared.models.PostWithoutDetails
import com.lintang.shared.models.Theme
import com.lintang.shared.style.PostPreviewStyle
import com.lintang.shared.util.Constants.FONT_FAMILY
import com.lintang.shared.util.parseDateString
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
import com.varabyte.kobweb.compose.ui.modifiers.width
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
    selectable: Boolean = false,
    darkTheme: Boolean = false,
    isVertical: Boolean = true,
    thumbnailHeight: CSSSizeValue<CSSUnit.px> = 320.px,
    titleMaxLines: Int = 2,
    titleColor: CSSColorValue = Colors.Black,
    onSelect: (id: String) -> Unit = {},
    onDeselect: (id: String) -> Unit = {},
    onDetail: (id: String) -> Unit = {},
) {
    var checked by remember(selectable) { mutableStateOf(false) }
    if (!isVertical) {
        Row(
            PostPreviewStyle
                .toModifier()
                .then(modifier)
                .fillMaxWidth()
                .padding(bottom = 24.px)
                .cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectable = selectable,
                checked = checked,
                thumbnailHeight = thumbnailHeight,
                titleMaxLines = titleMaxLines,
                titleColor = titleColor,
                isVertical = isVertical,
            )
        }
    } else {
        Column(
            modifier = PostPreviewStyle
                .toModifier()
                .then(modifier)
                .fillMaxWidth(
                    if (darkTheme || titleColor == Theme.Sponsored.rgb)
                        100.percent else 95.percent
                )
                .margin(bottom = 24.px)
                .padding(all = if (selectable) 10.px else 0.px)
                .borderRadius(4.px)
                .border(
                    width = if (selectable) 4.px else 0.px,
                    style = if (selectable) LineStyle.Solid else LineStyle.None,
                    color = if (checked) Theme.Primary.rgb else Theme.Gray.rgb
                )
                .transition(CSSTransition(property = TransitionProperty.All, 200.ms))
                .onClick {
                    if (selectable) {
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
                .cursor(Cursor.Pointer)
        ) {

            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectable = selectable,
                checked = checked,
                thumbnailHeight = thumbnailHeight,
                titleMaxLines = titleMaxLines,
                isVertical = isVertical,
                titleColor = titleColor,
            )
        }
    }


}

@Composable
fun PostContent(
    post: PostWithoutDetails,
    darkTheme: Boolean,
    selectable: Boolean,
    checked: Boolean,
    thumbnailHeight: CSSSizeValue<CSSUnit.px> = 320.px,
    titleMaxLines: Int = 2,
    titleColor: CSSColorValue,
    isVertical: Boolean,
) {
    Image(
        modifier = Modifier
            .margin(bottom = if (darkTheme) 20.px else 16.px)
            .height(thumbnailHeight)
            .fillMaxWidth()
            .thenIf(!isVertical, Modifier.width((thumbnailHeight.value * 1.5).px))
            .objectFit(ObjectFit.Contain),
        src = post.thumbnail,
        description = "post thumbnail"
    )
    Column(
        modifier = Modifier.thenIf(
            condition = !isVertical,
            other = Modifier.margin(left = 20.px)
                .fillMaxWidth()
        )
    ) {
        SpanText(
            modifier = Modifier.fontFamily(FONT_FAMILY)
                .fontSize(12.px)
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier.margin(bottom = 12.px)
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
                    property("-webkit-box-orient", "vertical")
                },
            text = post.title,
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryChip(
                post.category,
                darkTheme = darkTheme
            )
            if (selectable) {
                CheckboxInput(
                    attrs =
                    Modifier.size(20.px).toAttrs(),
                    checked = checked
                )
            }
        }
    }

}

