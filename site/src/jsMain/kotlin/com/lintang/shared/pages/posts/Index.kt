package com.lintang.shared.pages.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.shared.components.CategoryNavigationItems
import com.lintang.shared.components.ErrorView
import com.lintang.shared.components.LoadingIndicator
import com.lintang.shared.components.OverFlowSidePanel
import com.lintang.shared.models.ApiResponse
import com.lintang.shared.models.Constants.POST_ID_PARAM
import com.lintang.shared.models.Post
import com.lintang.shared.models.Theme
import com.lintang.shared.section.FooterSection
import com.lintang.shared.section.HeaderSection
import com.lintang.shared.util.Constants.FONT_FAMILY
import com.lintang.shared.util.Id
import com.lintang.shared.util.Res
import com.lintang.shared.util.getPostById
import com.lintang.shared.util.parseDateString
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
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
    var response by remember { mutableStateOf<ApiResponse>(ApiResponse.Idle) }
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()

    LaunchedEffect(context.route) {
        if (hasParams) {
            response = getPostById(context.route.params[POST_ID_PARAM] ?: "")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overFlowMenuOpened) {
            OverFlowSidePanel(content = {
                CategoryNavigationItems(true, onMenuClose = {
                    overFlowMenuOpened = false
                })
            }, onMenuClose = {
                overFlowMenuOpened = !overFlowMenuOpened
            })
        }
        HeaderSection(
            breakpoint = breakpoint,
            logo = Res.Image.logo,
            onMenuOpen = {
                overFlowMenuOpened = true
            },
            selectedCategory = null,
        )

        when (response) {
            is ApiResponse.Idle -> {
                LoadingIndicator()
            }

            is ApiResponse.Success -> {
                PostPageContent(post = (response as ApiResponse.Success).data)
                scope.launch {
                    delay(50)
                    try {
                        js("hljs.highlightAll()") as Unit
                    } catch (e: Exception) {
                        println("error highlight $e")
                    }

                }
            }

            is ApiResponse.Error -> {
                ErrorView((response as ApiResponse.Error).message)
            }

        }
        Box(Modifier.weight(1f))
        FooterSection()
    }


}

@Composable
fun PostPageContent(post: Post) {
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
                .fontFamily(FONT_FAMILY)

                .fontSize(14.px)
                .color(Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .margin(bottom = 20.px)
                .fillMaxWidth()
                .color(Theme.Black.rgb)
                .fontSize(40.px)
                .fontWeight(FontWeight.Bold)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .textAlign(TextAlign.Center)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "3")
                    property("line-clamp", "3")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.title
        )
        Image(
            modifier = Modifier
                .margin(bottom = 48.px)
                .fillMaxWidth()
                .maxHeight(600.px),
            src = post.thumbnail
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