package com.lintang.multiplatform.pages.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.components.CategoryNavigationItems
import com.lintang.multiplatform.components.ErrorView
import com.lintang.multiplatform.components.LoadingIndicator
import com.lintang.multiplatform.components.OverFlowSidePanel
import com.lintang.multiplatform.models.ApiResponse
import com.lintang.multiplatform.models.Constants.POST_ID_PARAM
import com.lintang.multiplatform.models.Post
import com.lintang.multiplatform.section.FooterSection
import com.lintang.multiplatform.section.HeaderSection
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Id
import com.lintang.multiplatform.util.Res
import com.lintang.multiplatform.util.getPostById
import com.lintang.multiplatform.util.parseDateString
import com.lintang.shared.Constants.SHOW_SECTIONS_PARAM
import com.lintang.shared.JsTheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Page(routeOverride = "post")
@Composable
fun PostPage() {
    val context = rememberPageContext()
    val hasParams = remember(context.route) { context.route.params.containsKey(POST_ID_PARAM) }
    var apiResponse by remember { mutableStateOf<ApiResponse>(ApiResponse.Idle) }
    var overflowOpened by remember { mutableStateOf(false) }
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    var showSections by remember { mutableStateOf(true) }

    LaunchedEffect(context.route) {
        showSections = if (context.route.params.containsKey(SHOW_SECTIONS_PARAM)) {
            (context.route.params[SHOW_SECTIONS_PARAM] ?: "false").toBoolean()
        } else {
            false
        }
        if (hasParams) {
            apiResponse = getPostById(context.route.params[POST_ID_PARAM] ?: "")
        }
    }


    if (overflowOpened) {
        OverFlowSidePanel(
            onMenuClose = { overflowOpened = false },
            content = { CategoryNavigationItems(isVertical = true) }
        )
    }
    if (showSections) {
        HeaderSection(
            breakpoint = breakpoint,
            logo = Res.Image.logo,
            onMenuOpen = { overflowOpened = true }
        )
    }
    when (apiResponse) {
        is ApiResponse.Success -> {
            PostPageContent(
                post = (apiResponse as ApiResponse.Success).data,
                breakpoint = breakpoint
            )
            scope.launch {
                delay(50)
                try {
                    js("hljs.highlightAll()") as Unit
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }

        is ApiResponse.Idle -> {
            LoadingIndicator()
        }

        is ApiResponse.Error -> {
            ErrorView(message = (apiResponse as ApiResponse.Error).message)
        }
    }
    if (showSections) {
        FooterSection()
    }

}

@Composable
fun PostPageContent(
    post: Post,
    breakpoint: Breakpoint
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 50.px, bottom = 100.px)
            .maxWidth(800.px),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(post) {
            (document.getElementById(Id.postContent) as HTMLDivElement).innerHTML = post.content
        }
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .color(JsTheme.HalfBlack.rgb)
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .margin(bottom = 20.px)
                .color(Colors.Black)
                .fontFamily(FONT_FAMILY)
                .fontSize(40.px)
                .fontWeight(FontWeight.Bold)
                .overflow(Overflow.Hidden)
                .textOverflow(TextOverflow.Ellipsis)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "2")
                    property("line-clamp", "2")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.title
        )
        Image(
            modifier = Modifier
                .margin(bottom = 40.px)
                .fillMaxWidth()
                .objectFit(ObjectFit.Cover)
                .height(
                    if (breakpoint <= Breakpoint.SM) 250.px
                    else if (breakpoint <= Breakpoint.MD) 400.px
                    else 600.px
                ),
            src = post.thumbnail,
        )
        Div(
            attrs = Modifier
                .id(Id.postContent)
                .fontFamily(FONT_FAMILY)
                .fillMaxWidth()
                .toAttrs()
        )
    }
}