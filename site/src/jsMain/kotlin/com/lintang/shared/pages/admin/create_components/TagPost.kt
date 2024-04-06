package com.lintang.shared.pages.admin.create_components

import androidx.compose.runtime.Composable
import com.lintang.shared.models.Theme
import com.lintang.shared.util.Constants
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px

@Composable
fun TagPost(
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
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(14.px)
                .color(Theme.HalfBlack.rgb),
            text = title,
        )
    }
}
