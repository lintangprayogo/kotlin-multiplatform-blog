package com.lintang.multiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.components.AdminPageLayout
import com.lintang.multiplatform.components.Posts
import com.lintang.multiplatform.components.SearchBar
import com.lintang.multiplatform.models.ApiListResponse
import com.lintang.multiplatform.models.PostWithoutDetails
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Constants.POST_PER_REQUEST
import com.lintang.multiplatform.util.deleteSelectedPost
import com.lintang.multiplatform.util.getMyPost
import com.lintang.multiplatform.util.isUserLoggedIn
import com.lintang.multiplatform.util.noBorder
import com.lintang.multiplatform.util.parseSelectedPostList
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Visibility
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
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button

//routing ada pada

@Page
@Composable
fun MyPostPage() {
    isUserLoggedIn {
        MyPostScreen()
    }
}

@Composable
fun MyPostScreen() {
    val breakpoint = rememberBreakpoint()
    val myposts = remember { mutableStateListOf<PostWithoutDetails>() }
    var selectable by remember { mutableStateOf(false) }
    var switchText by remember { mutableStateOf("Select ") }
    var isShowMoreVisibility by remember { mutableStateOf(true) }
    var postSkip by remember { mutableStateOf(0) }
    val selectedPosts = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        getMyPost(skip = 0, onSuccess = {
            if (it is ApiListResponse.Success) {
                myposts.clear()
                myposts.addAll(it.data)
                isShowMoreVisibility = it.data.size >= POST_PER_REQUEST
                postSkip += POST_PER_REQUEST
            }
            if (it is ApiListResponse.Error) {
                println("SOMETHING BAD HAPPEN ${it.message}")
            }
        }, onError = {
            println("SOMETHING BAD HAPPEN $it")
        })
    }


    AdminPageLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(left = if (breakpoint > Breakpoint.MD) Constants.SIDE_PANEL_WIDTH.px else 0.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(
                    if (breakpoint > Breakpoint.MD) 30.percent
                    else 60.percent
                )
            ) {
                SearchBar(
                    breakpoint = breakpoint,
                    onEnterClick = {
                    }, onSearchIconClick = {

                    })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(
                        if (breakpoint > Breakpoint.MD) 80.percent
                        else 90.percent
                    )
                    .margin(bottom = 24.px),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        size = SwitchSize.LG,
                        checked = selectable,
                        onCheckedChange = {
                            selectable = it
                            if (!selectable) {
                                switchText = "Select"
                                selectedPosts.clear()
                            } else {
                                switchText = "0 Posts Selected"
                            }
                        }
                    )
                    SpanText(
                        modifier = Modifier.color(if (selectable) Colors.Black else Theme.HalfBlack.rgb),
                        text = switchText
                    )
                }
                Button(
                    attrs = Modifier
                        .margin(right = 20.px)
                        .height(54.px)
                        .padding(leftRight = 24.px)
                        .backgroundColor(Theme.Red.rgb)
                        .color(Colors.White)
                        .noBorder()
                        .borderRadius(r = 4.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .fontWeight(FontWeight.Medium)
                        .visibility(if (selectedPosts.isNotEmpty()) Visibility.Visible else Visibility.Hidden)
                        .onClick {
                            println("my selected ${selectedPosts.size}")
                            scope.launch {
                                val result = deleteSelectedPost(selectedPosts)
                                if (result) {
                                    selectable = false
                                    switchText = "Select"
                                    postSkip -= selectedPosts.size
                                    selectedPosts.forEach { deletedPostId ->
                                        myposts.removeAll {
                                            it._id == deletedPostId
                                        }
                                    }
                                    selectedPosts.clear()
                                }
                            }
                        }.toAttrs(),
                ) {
                    SpanText("Delete")
                }

            }

            Posts(
                breakpoint = breakpoint,
                posts = myposts,
                isShowMoreVisibility = isShowMoreVisibility,
                onShowMore = {
                    scope.launch {
                        getMyPost(skip = postSkip, onSuccess = {
                            if (it is ApiListResponse.Success) {
                                if (it.data.isNotEmpty()) {
                                    myposts.addAll(it.data)
                                    if (it.data.size < POST_PER_REQUEST) isShowMoreVisibility =
                                        false
                                    postSkip += POST_PER_REQUEST
                                } else {
                                    isShowMoreVisibility = false
                                }
                            }
                            if (it is ApiListResponse.Error) {
                                println("SOMETHING BAD HAPPEN ${it.message}")
                            }
                        }, onError = {
                            println("SOMETHING BAD HAPPEN $it")
                        }

                        )
                    }
                },
                onDeselect = { id ->
                    selectedPosts.remove(id)
                    switchText = parseSelectedPostList(selectedPosts)
                },
                onSelect = { id ->
                    selectedPosts.add(id)
                    switchText = parseSelectedPostList(selectedPosts)
                },
                selectable = selectable

            )


        }


    }
}