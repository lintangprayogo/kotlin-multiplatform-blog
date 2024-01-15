package com.lintang.multiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.components.AdminPageLayout
import com.lintang.multiplatform.components.Posts
import com.lintang.multiplatform.components.SearchBar
import com.lintang.multiplatform.models.ApiListResponse
import com.lintang.multiplatform.models.PostWithoutDetails
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.util.Constants
import com.lintang.multiplatform.util.getMyPost
import com.lintang.multiplatform.util.isUserLoggedIn
import com.lintang.multiplatform.util.noBorder
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
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
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
    var myposts = remember { mutableStateListOf<PostWithoutDetails>() }
    var selectable by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("Select ") }

    LaunchedEffect(Unit) {
        getMyPost(skip = 0, onSuccess = {
            if(it is ApiListResponse.Success){
                myposts.addAll(it.data)
            }
            if(it is ApiListResponse.Error){
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

            Row(
                modifier = Modifier
                    .fillMaxWidth(90.percent)
                    .margin(bottom = 24.px),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Switch(
                        modifier = Modifier.margin(right = 10.px),
                        size = SwitchSize.LG,
                        checked = selectable,
                        onCheckedChange = {
                            selectable = it
                        })
                    SpanText(
                        modifier = Modifier
                            .color(if (selectable) Colors.Black else Theme.HalfBlack.rgb)
                            .fontFamily(Constants.FONT_FAMILY)
                            .fontSize(16.px),
                        text = text
                    )
                }
                Button(
                    attrs = Modifier.height(54.px)
                        .padding(topBottom = 14.px, leftRight = 20.px)
                        .backgroundColor(Theme.Red.rgb)
                        .color(Colors.White)
                        .fontFamily(Constants.FONT_FAMILY)
                        .fontSize(14.px)
                        .borderRadius(4.px)
                        .noBorder()
                        .onClick {

                        }
                        .toAttrs()
                ) {
                    SpanText("Delete")
                }

            }

            Posts(breakpoint,myposts)


        }


    }
}