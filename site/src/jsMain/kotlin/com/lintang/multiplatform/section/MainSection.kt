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
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable

fun MainSection(
    breakpoint: Breakpoint,
    response: ApiListResponse,
    onDetail: (id: String) -> Unit = {},
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
                    MainPostContent(
                        breakpoint = breakpoint,
                        posts = response.data,
                        onDetail = onDetail
                    )
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
    posts: List<PostWithoutDetails>,
    onDetail: (id: String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(
                if (breakpoint > Breakpoint.MD) 80.percent
                else 90.percent
            )
            .margin(topBottom = 50.px)
    ) {
        if (breakpoint == Breakpoint.XL) {
            PostPreview(
                post = posts.first(),
                darkTheme = true,
                thumbnailHeight = 640.px,
                onDetail = { onDetail(posts.first()._id) }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(80.percent)
                    .margin(left = 20.px)
            ) {
                posts.drop(1).forEach { postWithoutDetails ->
                    PostPreview(
                        modifier = Modifier.margin(bottom = 20.px),
                        post = postWithoutDetails,
                        darkTheme = true,
                        isVertical = false,
                        thumbnailHeight = 200.px,
                        titleMaxLines = 1,
                        onDetail = { onDetail(postWithoutDetails._id) }
                    )
                }
            }
        } else if (breakpoint >= Breakpoint.LG) {
            Box(modifier = Modifier.margin(right = 10.px)) {
                PostPreview(
                    post = posts.first(),
                    darkTheme = true,
                    onDetail = { onDetail(posts.first()._id) }
                )
            }
            Box(modifier = Modifier.margin(left = 10.px)) {
                PostPreview(
                    post = posts[1],
                    darkTheme = true,
                    onDetail = { onDetail(posts[1]._id) }
                )
            }
        } else {
            PostPreview(
                post = posts.first(),
                darkTheme = true,
                thumbnailHeight = 640.px,
                onDetail = { onDetail(posts.first()._id) }
            )
        }
    }
}
