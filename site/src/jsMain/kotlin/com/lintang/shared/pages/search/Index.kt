package com.lintang.shared.pages.search

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
import com.lintang.shared.components.LoadingIndicator
import com.lintang.shared.components.OverFlowSidePanel
import com.lintang.shared.models.ApiListResponse
import com.lintang.shared.models.Constants
import com.lintang.shared.models.Constants.CATEGORY_PARAM
import com.lintang.shared.models.Constants.TITLE_PARAM
import com.lintang.shared.models.PostWithoutDetails
import com.lintang.shared.section.FooterSection
import com.lintang.shared.section.HeaderSection
import com.lintang.shared.section.PostSection
import com.lintang.shared.util.Constants.FONT_FAMILY
import com.lintang.shared.util.Id
import com.lintang.shared.util.Res
import com.lintang.shared.util.searchPostByCategory
import com.lintang.shared.util.searchPostByTitle
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement
import com.lintang.shared.Category
@Page(routeOverride = "query")
@Composable
fun SearchPage() {
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    val scope = rememberCoroutineScope()
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    val searchPost = remember { mutableStateListOf<PostWithoutDetails>() }
    var postToSkip by remember { mutableStateOf(0) }
    var isShowMoreVisibility by remember { mutableStateOf(true) }
    val hasCategoryParam =
        remember(context.route) { context.route.params.containsKey(CATEGORY_PARAM) }

    val hasTitleParam =
        remember(context.route) { context.route.params.containsKey(TITLE_PARAM) }

    val value = remember(context.route) {
        if (hasTitleParam) {
            context.route.params.getValue(TITLE_PARAM)
        } else if (hasCategoryParam) {
            context.route.params.getValue(CATEGORY_PARAM)
        } else {
            ""
        }
    }

    var apiListResponse by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }

    LaunchedEffect(key1 = context.route) {
        isShowMoreVisibility = false
        (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = ""
        if (hasTitleParam) {
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = value
            searchPostByTitle(
                title = value,
                skip = 0,
                onError = {
                    println(it)
                }, onSuccess = {
                    apiListResponse = it
                    if (it is ApiListResponse.Success) {
                        searchPost.clear()
                        searchPost.addAll(it.data)
                        isShowMoreVisibility = it.data.size >= Constants.POST_PER_REQUEST
                        postToSkip = Constants.POST_PER_REQUEST
                    }
                    if (it is ApiListResponse.Error) {
                        println("SOMETHING BAD HAPPEN ${it.message}")
                    }
                })
        } else if (hasCategoryParam) {
            searchPostByCategory(
                category = runCatching { Category.valueOf(value) }.getOrElse { Category.Technology },
                skip = 0,
                onError = {
                    println(it)
                }, onSuccess = {
                    if (it is ApiListResponse.Success) {
                        apiListResponse = it
                        searchPost.clear()
                        searchPost.addAll(it.data)
                        isShowMoreVisibility = it.data.size >= Constants.POST_PER_REQUEST
                        postToSkip = Constants.POST_PER_REQUEST
                    }
                    if (it is ApiListResponse.Error) {
                        println("SOMETHING BAD HAPPEN ${it.message}")
                    }
                })
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
            selectedCategory = if (hasTitleParam) null else runCatching { Category.valueOf(value) }.getOrElse { Category.Technology },
        )

        if (apiListResponse is ApiListResponse.Success) {
            if (hasCategoryParam) {
                SpanText(
                    modifier = Modifier.fillMaxWidth()
                        .textAlign(TextAlign.Center)
                        .margin(top = 100.px, bottom = 40.px)
                        .fontSize(35.px)
                        .fontFamily(FONT_FAMILY),
                    text = value.ifEmpty { Category.Technology.name },
                )
            }

            PostSection(
                breakpoint = breakpoint,
                posts = searchPost,
                onDetail = {
                    context.router.navigateTo(Screen.PostPage.editPostId(it))
            }, showMore = {
                scope.launch {
                    if (hasCategoryParam) {
                        searchPostByCategory(category = Category.valueOf(value),
                            skip = postToSkip,
                            onError = {
                                println(it)
                            },
                            onSuccess = {
                                if (it is ApiListResponse.Success) {
                                    if (it.data.isNotEmpty()) {
                                        searchPost.addAll(it.data)
                                        if (it.data.size < Constants.POST_PER_REQUEST) isShowMoreVisibility =
                                            false
                                        postToSkip += Constants.POST_PER_REQUEST
                                    } else {
                                        isShowMoreVisibility = false
                                    }
                                }
                                if (it is ApiListResponse.Error) {
                                    println("SOMETHING BAD HAPPEN ${it.message}")
                                }
                            })
                    } else {
                        searchPostByTitle(
                            title = value,
                            skip = postToSkip,
                            onError = {
                                println(it)
                            },
                            onSuccess = {
                                if (it is ApiListResponse.Success) {
                                    if (it.data.isNotEmpty()) {
                                        searchPost.addAll(it.data)
                                        if (it.data.size < Constants.POST_PER_REQUEST) isShowMoreVisibility =
                                            false
                                        postToSkip += Constants.POST_PER_REQUEST
                                    } else {
                                        isShowMoreVisibility = false
                                    }
                                }
                                if (it is ApiListResponse.Error) {
                                    println("SOMETHING BAD HAPPEN ${it.message}")
                                }
                            })
                    }

                }


            }, showMoreVisibility = isShowMoreVisibility
            )
        } else {
            LoadingIndicator()
        }
        Box(Modifier.weight(1f))
        FooterSection()

    }

}