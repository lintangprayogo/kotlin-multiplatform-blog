package com.lintang.multiplatform.section

import androidx.compose.runtime.Composable
import com.lintang.multiplatform.components.PostsView
import com.lintang.multiplatform.models.PostWithoutDetails
import com.lintang.multiplatform.util.Constants.PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.px

@Composable
fun PostSection(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    title: String,
    showMoreVisibility: Boolean,
    showMore: () -> Unit,
    onDetail: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .margin(topBottom = 15.px)
            .maxWidth(PAGE_WIDTH.px),
        contentAlignment = Alignment.TopCenter
    ) {
        PostsView(
            breakpoint = breakpoint,
            posts = posts,
            title = title,
            isShowMoreVisibility = showMoreVisibility,
            onDetail = onDetail,
            onShowMore = showMore
        )
    }
}