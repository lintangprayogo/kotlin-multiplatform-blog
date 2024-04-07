package com.lintang.multiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.Screen
import com.lintang.multiplatform.components.AdminPageLayout
import com.lintang.multiplatform.components.LoadingIndicator
import com.lintang.multiplatform.models.RandomJoke
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Constants.PAGE_WIDTH
import com.lintang.multiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.lintang.multiplatform.util.Res
import com.lintang.multiplatform.util.getRandomJoke
import com.lintang.multiplatform.util.isUserLoggedIn
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh


@Page
@Composable
fun HomePage() {
    isUserLoggedIn {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    var randomJoke by remember { mutableStateOf<RandomJoke?>(null) }

    LaunchedEffect(Unit) {
        getRandomJoke {
            randomJoke = it
        }
    }

    AdminPageLayout {
        AddButton()
        HomeContent(randomJoke)
    }
}

@Composable
fun HomeContent(joke: RandomJoke?) {
    val breakpoint = rememberBreakpoint()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
        contentAlignment = Alignment.Center
    ) {
        if (joke != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(topBottom = 50.px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (joke.id != -1) {
                    Image(
                        modifier = Modifier.size(150.px).margin(bottom = 50.px),
                        src = Res.Image.laugh, description = "laugh image"
                    )
                }

                if (joke.joke.contains("q:", true)) {
                    SpanText(
                        modifier = Modifier.fillMaxWidth(60.percent).textAlign(TextAlign.Center)
                            .color(Theme.Secondary.rgb).fontFamily(FONT_FAMILY).fontSize(28.px)
                            .fontWeight(
                                FontWeight.Bold
                            ),
                        text = joke.joke.split(":")[1].dropLast(1)
                    )
                    SpanText(
                        modifier = Modifier.fillMaxWidth(60.percent)
                            .textAlign(TextAlign.Center)
                            .color(Theme.HalfBlack.rgb)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(20.px)
                            .fontWeight(
                                FontWeight.Normal
                            ),
                        text = joke.joke.split(":").last()
                    )
                } else {
                    SpanText(
                        modifier = Modifier.fillMaxWidth(60.percent).fontFamily(FONT_FAMILY)
                            .textAlign(TextAlign.Center)
                            .color(Theme.Secondary.rgb).fontSize(28.px).fontWeight(
                                FontWeight.Bold
                            ),
                        text = joke.joke
                    )
                }

            }
        } else {
           LoadingIndicator()
        }
    }
}

@Composable
fun AddButton() {
    val breakpoint = rememberBreakpoint()
    val page = rememberPageContext()
    Box(
        modifier = Modifier.height(100.vh).fillMaxWidth().maxWidth(PAGE_WIDTH.px)
            .position(Position.Fixed).styleModifier {
                property("pointer-events", "none")
            },
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier.size(if (breakpoint > Breakpoint.MD) 80.px else 50.px)
                .borderRadius(15.px)
                .cursor(Cursor.Pointer)
                .styleModifier {
                    property("pointer-events", "auto")
                }
                .onClick { page.router.navigateTo(Screen.AdminCreate.route) }
                .margin(
                    right = if (breakpoint > Breakpoint.MD) 50.px else 20.px,
                    bottom = if (breakpoint > Breakpoint.MD) 50.px else 20.px,
                ).background(Theme.Primary.rgb),
            contentAlignment = Alignment.Center
        ) {
            FaPlus(
                modifier = Modifier.color(Colors.White),
                size = IconSize.LG
            )
        }

    }
}