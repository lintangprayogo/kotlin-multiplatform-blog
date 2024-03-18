package com.lintang.multiplatform.section

import androidx.compose.runtime.Composable
import com.lintang.multiplatform.components.PostPreview
import com.lintang.multiplatform.models.ApiListResponse
import com.lintang.multiplatform.models.PostWithoutDetails
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable

fun MainSection(
    breakpoint: Breakpoint,
    response: ApiListResponse
) {
    Box(
        Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(Constants.PAGE_WIDTH.px),
            contentAlignment = Alignment.Center
        ) {
            when (response) {
                is ApiListResponse.Idle -> {

                }

                is ApiListResponse.Success -> {
                    MainPostContent(breakpoint = breakpoint, posts = response.data)
                }

                is ApiListResponse.Error -> {
                    println(response.message)
                }
            }

        }
    }

}

@Composable
fun MainPostContent(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>
) {
    Row(modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)) {
        if (breakpoint == Breakpoint.XL) {
            posts.firstOrNull()?.let { post ->
                PostPreview(
                    post = post, darkTheme = true,
                    thumbnailHeight = 640.px,
                    isSameWidth = false
                )
            }
            Column(
                modifier =
                Modifier
                    .fillMaxWidth(45.percent)
                    .margin(left = 50.px)
            ) {
                posts.drop(1).forEach { post ->
                    PostPreview(
                        post = post, darkTheme = true,
                        isVertical = false,
                        thumbnailHeight = 200.px,
                        titleMaxLines = 1
                    )
                }
            }
        } else {
            if (posts.size >= 2 && breakpoint >= Breakpoint.LG) {
                Box(modifier = Modifier.margin(right = 10.px)) {
                    PostPreview(
                        post = posts[1],
                        darkTheme = true,
                        isSameWidth = false
                    )
                }
                Box(modifier = Modifier.margin(left = 10.px)) {
                    PostPreview(
                        post = posts[2],
                        darkTheme = true,
                        isSameWidth = false
                    )
                }
            } else if (posts.isNotEmpty()) {
                posts.firstOrNull()?.let { post ->
                    PostPreview(
                        post = post,
                        darkTheme = true,
                        isSameWidth = false
                    )
                }
            }

        }

    }
}
