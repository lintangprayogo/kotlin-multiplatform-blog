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
import com.lintang.multiplatform.util.Constants
import com.lintang.multiplatform.util.getMyPost
import com.lintang.multiplatform.util.isUserLoggedIn
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

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
    var postToSkip by remember { mutableStateOf(0) }
    var isShowMoreVisibility by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        getMyPost(skip = 0, onSuccess = {
            if (it is ApiListResponse.Success) {
                myposts.clear()
                myposts.addAll(it.data)
                postToSkip = it.data.size
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
                    breakpoint = breakpoint, onEnterClick = {

                    }, onSearchIconClick = {

                    })
            }



            Posts(
                breakpoint = breakpoint,
                posts = myposts,
                isShowMoreVisibility = isShowMoreVisibility,
                onShowMore = {
                    scope.launch {
                        getMyPost(skip = myposts.size - 1,
                            onSuccess = {
                                if (it is ApiListResponse.Success) {
                                    isShowMoreVisibility = it.data.isNotEmpty()
                                    myposts.addAll(it.data)
                                }
                                if (it is ApiListResponse.Error) {
                                    println("SOMETHING BAD HAPPEN ${it.message}")
                                }
                            }, onError = {
                                println("SOMETHING BAD HAPPEN $it")
                            })
                    }
                })


        }


    }
}