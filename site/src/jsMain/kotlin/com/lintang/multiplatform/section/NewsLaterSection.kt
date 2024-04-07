package com.lintang.multiplatform.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.components.MessagePopup
import com.lintang.multiplatform.models.NewsLater
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.style.NewsletterInputStyle
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Constants.PAGE_WIDTH
import com.lintang.multiplatform.util.Id
import com.lintang.multiplatform.util.isEmailValid
import com.lintang.multiplatform.util.noBorder
import com.lintang.multiplatform.util.subscribeNews
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement


@Composable
fun NewsLaterSection(breakpoint: Breakpoint) {
    val scope = rememberCoroutineScope()
    var responseMessage by remember { mutableStateOf("") }
    var invalidPopup by remember { mutableStateOf(false) }
    var subscribedPopup by remember { mutableStateOf(false) }

    if (invalidPopup) {
        MessagePopup(
            message = "Email Is Invalid !!!",
            onMessageDismiss = {
                invalidPopup = false
            })
    } else if (subscribedPopup) {
        MessagePopup(
            message = responseMessage,
            onMessageDismiss = {
                invalidPopup = false
            })


    }

    Box(
        modifier = Modifier
            .margin(topBottom = 250.px)
            .fillMaxWidth()
            .maxWidth(PAGE_WIDTH.px)
    ) {
        NewsLetterContent(breakpoint = breakpoint,
            onInvalidEmail = {
                scope.launch {
                    invalidPopup = true
                    delay(2000)
                    invalidPopup = false
                }

            }, onSubscribe = {
                scope.launch {
                    subscribedPopup = true
                    responseMessage = it
                    delay(2000)
                    subscribedPopup = false
                    responseMessage = ""
                }

            })


    }
}

@Composable
fun NewsLetterContent(
    breakpoint: Breakpoint,
    onSubscribe: (String) -> Unit,
    onInvalidEmail: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SpanText(
            modifier = Modifier.fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(36.px)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center),
            text = "Don't miss any News Post."
        )
        SpanText(
            modifier = Modifier.fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(36.px)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center),
            text = "Sign up to our Newsletter."
        )
        SpanText(
            modifier = Modifier.fillMaxWidth()
                .margin(top = 6.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(18.px)
                .fontWeight(FontWeight.Normal)
                .textAlign(TextAlign.Center),
            text = "Keep up with the latest news and blogs."
        )

        if (breakpoint > Breakpoint.SM) {
            Row(
                Modifier.fillMaxWidth().margin(top = 40.px),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SubscribeForm(false, breakpoint, onSubscribe, onInvalidEmail)

            }
        } else {
            Column(
                Modifier.fillMaxWidth().margin(top = 40.px),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SubscribeForm(true, breakpoint, onSubscribe, onInvalidEmail)
            }
        }
    }

}

@Composable
fun SubscribeForm(
    vertical: Boolean,
    breakpoint: Breakpoint,
    onSubscribe: (String) -> Unit,
    onInvalidEmail: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Input(
        type = InputType.Text,
        attrs = NewsletterInputStyle.toModifier()
            .id(Id.emailInput)
            .width(320.px)
            .height(54.px)
            .color(Theme.DarkGray.rgb)
            .backgroundColor(Theme.Gray.rgb)
            .padding(leftRight = 24.px)
            .margin(
                right = if (vertical) 0.px else 20.px,
                bottom = if (vertical) 20.px else 0.px
            )
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .borderRadius(100.px)
            .toAttrs {
                attr("placeholder", "Your Email Address")
            }
    )

    Button(attrs = Modifier
        .onClick {
            val email = (document.getElementById(Id.emailInput) as HTMLInputElement).value
            if (isEmailValid(email = email)) {
                scope.launch {
                    val result = subscribeNews(NewsLater(email = email))
                    onSubscribe(result)
                }
            } else {
                onInvalidEmail()
            }
        }
        .height(54.px)
        .borderRadius(100.px)
        .backgroundColor(Theme.Primary.rgb)
        .padding(leftRight = 50.px)
        .noBorder()
        .cursor(Cursor.Pointer)
        .toAttrs()) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(18.px)
                .fontWeight(FontWeight.Normal)
                .color(Colors.White),
            text = "Subscribe"
        )
    }
}