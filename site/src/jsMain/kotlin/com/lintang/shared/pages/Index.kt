package com.lintang.shared.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.shared.Screen
import com.lintang.shared.components.CategoryNavigationItems
import com.lintang.shared.components.OverFlowSidePanel
import com.lintang.shared.models.ApiListResponse
import com.lintang.shared.models.Constants.POST_PER_REQUEST
import com.lintang.shared.models.PostWithoutDetails
import com.lintang.shared.section.FooterSection
import com.lintang.shared.section.HeaderSection
import com.lintang.shared.section.MainSection
import com.lintang.shared.section.NewsLaterSection
import com.lintang.shared.section.PostSection
import com.lintang.shared.section.SponsoredPostsSections
import com.lintang.shared.util.getLastestPost
import com.lintang.shared.util.getMainPost
import com.lintang.shared.util.getPopularPost
import com.lintang.shared.util.getSponsoredPost
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch

@Page
@Composable
fun HomePage() {
    val breakpoint = rememberBreakpoint()
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }

    val latestPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var latestPostSkip by remember { mutableStateOf(0) }
    var latestShowMoreVisibility by remember { mutableStateOf(false) }

    val popularPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var popularPostSkip by remember { mutableStateOf(0) }
    var popularShowMoreVisibility by remember { mutableStateOf(false) }

    val sponsoredPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()

    LaunchedEffect(Unit) {
        getMainPost(
            onError = {

            },
            onSuccess = { result ->
                mainPosts = result
            })

        latestPostSkip = 0
        latestPosts.clear()
        getLastestPost(
            onError = {
                println(it)
            },
            onSuccess = { result ->
                when (result) {
                    is ApiListResponse.Success -> {
                        latestPosts.addAll(result.data)
                        latestPostSkip = POST_PER_REQUEST
                        latestShowMoreVisibility = result.data.size >= POST_PER_REQUEST
                    }

                    is ApiListResponse.Error -> {
                        println(result.message)
                        latestShowMoreVisibility = false
                    }

                    else -> {}
                }
            },
            skip = latestPostSkip
        )

        getSponsoredPost(
            onError = {
                println(it)
            },
            onSuccess = { result ->
                when (result) {
                    is ApiListResponse.Success -> {
                        sponsoredPosts.addAll(result.data)
                    }

                    is ApiListResponse.Error -> {
                        println(result.message)
                    }

                    else -> {}
                }
            },
        )
        popularPostSkip = 0
        popularPosts.clear()
        getPopularPost(
            onError = {
                println(it)
            },
            onSuccess = { result ->
                when (result) {
                    is ApiListResponse.Success -> {
                        popularPosts.addAll(result.data)
                        popularPostSkip = POST_PER_REQUEST
                        popularShowMoreVisibility = result.data.size >= POST_PER_REQUEST
                    }

                    is ApiListResponse.Error -> {
                        println(result.message)
                        popularShowMoreVisibility = false
                    }

                    else -> {}
                }
            },
            skip = popularPostSkip
        )

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (overFlowMenuOpened) {
            OverFlowSidePanel(content = {
                CategoryNavigationItems(true)
            }, onMenuClose = {
                overFlowMenuOpened = !overFlowMenuOpened
            })
        }
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpen = {
                overFlowMenuOpened = true
            })

        MainSection(breakpoint = breakpoint, response = mainPosts)

        PostSection(
            breakpoint = breakpoint,
            title = "Latest Post",
            posts = latestPosts,
            onDetail = {
                context.router.navigateTo(Screen.PostPage.editPostId(it))
            },
            showMore = {
                scope.launch {
                    getLastestPost(skip = latestPostSkip,
                        onSuccess = { result ->
                            when (result) {
                                is ApiListResponse.Success -> {
                                    if (result.data.isNotEmpty()) {
                                        latestPosts.addAll(result.data)

                                        latestShowMoreVisibility = result.data.size >= POST_PER_REQUEST
                                        latestPostSkip += POST_PER_REQUEST
                                    } else {
                                        latestShowMoreVisibility = false
                                    }
                                }

                                is ApiListResponse.Error -> {
                                    println(result.message)
                                    latestShowMoreVisibility = false
                                }

                                else -> {}
                            }
                        },
                        onError = {

                        }
                    )
                }

            },
            showMoreVisibility = latestShowMoreVisibility
        )

        SponsoredPostsSections(
            breakpoint = breakpoint,
            posts = sponsoredPosts,
            onDetail = {
                context.router.navigateTo(Screen.PostPage.editPostId(it))
            }
        )

        PostSection(
            breakpoint = breakpoint,
            title = "Popular Post",
            posts = popularPosts,
            onDetail = {
                context.router.navigateTo(Screen.PostPage.editPostId(it))
            },
            showMore = {
                scope.launch {
                    getPopularPost(
                        onError = {
                            println(it)
                        },
                        onSuccess = { result ->
                            when (result) {
                                is ApiListResponse.Success -> {
                                    popularPosts.addAll(result.data)
                                    popularPostSkip += POST_PER_REQUEST
                                    popularShowMoreVisibility = result.data.size >= POST_PER_REQUEST
                                }

                                is ApiListResponse.Error -> {
                                    println(result.message)
                                    popularShowMoreVisibility = false
                                }

                                else -> {}
                            }
                        },
                        skip = popularPostSkip
                    )
                }

            },
            showMoreVisibility = popularShowMoreVisibility
        )

        NewsLaterSection(breakpoint)

        Box(Modifier.weight(1f))
        FooterSection()



    }
}