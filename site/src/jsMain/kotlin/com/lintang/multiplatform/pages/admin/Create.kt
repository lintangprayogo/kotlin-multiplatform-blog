package com.lintang.multiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.components.AdminPageLayout
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.lintang.multiplatform.util.Id.subtitleInput
import com.lintang.multiplatform.util.Id.titleInput
import com.lintang.multiplatform.util.isUserLoggedIn
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Input


@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
private fun CreateScreen() {
    AdminPageLayout {
        CreateContent()
    }
}

@Composable
private fun CreateContent() {
    val breakpoint = rememberBreakpoint()
    var popularSwitch by remember { mutableStateOf(false) }
    var mainSwitch by remember { mutableStateOf(false) }
    var sponsorSwitch by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .margin(topBottom = 50.px)
            .padding(leftRight = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .maxWidth(700.px),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SimpleGrid(numColumns = numColumns(base = 1, sm = 3)) {
                TagPost(modifier = Modifier
                    .margin(
                        right = 24.px,
                        bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                    ),
                    title = "Popular",
                    checked = popularSwitch,
                    onCheck = { popularSwitch = it })
                TagPost(
                    modifier = Modifier
                        .margin(
                            right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                            bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                        ),
                    title = "Main",
                    checked = mainSwitch,
                    onCheck = { mainSwitch = it })
                TagPost(
                    title = "Sponsor",
                    checked = sponsorSwitch,
                    onCheck = { sponsorSwitch = it })
            }

            Input(
                attrs = Modifier.fillMaxWidth()
                    .id(titleInput)
                    .height(54.px)
                    .margin(topBottom = 12.px)
                    .padding(leftRight = 20.px)
                    .background(Theme.LightGrey.rgb)
                    .borderRadius(4.px)
                    .border(0.px, style = LineStyle.None, color = Colors.Transparent)
                    .outline(0.px, style = LineStyle.None, color = Colors.Transparent)
                    .toAttrs {
                        attr("placeholder", "Title")
                    },
                    type = InputType.Text,
                )
            Input(
                attrs = Modifier.fillMaxWidth()
                    .id(subtitleInput)
                    .height(54.px)
                    .margin(bottom = 12.px)
                    .padding(leftRight = 20.px)
                    .background(Theme.LightGrey.rgb)
                    .borderRadius(4.px)
                    .border(0.px, style = LineStyle.None, color = Colors.Transparent)
                    .outline(0.px, style = LineStyle.None, color = Colors.Transparent)
                    .toAttrs {
                        attr("placeholder", "Subtitle")
                    },
                type = InputType.Text,
            )
        }
    }
}

@Composable
private fun TagPost(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onCheck: (Boolean) -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Switch(
            modifier = Modifier.margin(right = 8.px),
            checked = checked,
            onCheckedChange = onCheck,
            size = SwitchSize.LG
        )
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .color(Theme.HalfBlack.rgb),
            text = title,
        )
    }
}