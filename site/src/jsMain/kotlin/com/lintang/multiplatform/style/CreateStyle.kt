package com.lintang.multiplatform.style

import com.lintang.multiplatform.models.Theme
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.hover
import org.jetbrains.compose.web.css.ms

val EditorKeyStyle by ComponentStyle {
    base {
        Modifier.backgroundColor(Colors.Transparent).transition(
            CSSTransition(
                "background", duration = 300.ms
            )
        )
    }
    hover {
        Modifier.backgroundColor(Theme.Primary.rgb)
    }
}