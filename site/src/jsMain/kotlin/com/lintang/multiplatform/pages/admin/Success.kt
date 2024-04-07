package com.lintang.multiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.lintang.multiplatform.Screen
import com.lintang.multiplatform.models.Constants.UPDATED_PARAM
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants
import com.lintang.multiplatform.util.Res
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun SuccessPage() {
    val context = rememberPageContext()
    val isUpdated =
        remember(context.route) { (context.route.params[UPDATED_PARAM] ?: "false").toBoolean() }

    LaunchedEffect(Unit) {
        delay(5000)
        context.router.navigateTo(Screen.AdminCreate.route)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.margin(bottom = 24.px),
            src = Res.Icon.checkmark,
            description = "checkmark"
        )
        SpanText(
            modifier = Modifier.fontFamily(Constants.FONT_FAMILY).fontSize(24.px),
            text = if (isUpdated) "Post Successfully Updated!" else "Post Successfully Created!"
        )
        SpanText(
            modifier = Modifier
                .color(Theme.HalfBlack.rgb)
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(18.px),
            text = "Redirecting you back..."
        )
    }
}