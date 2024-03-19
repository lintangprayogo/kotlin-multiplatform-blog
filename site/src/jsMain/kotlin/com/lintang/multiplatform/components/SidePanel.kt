package com.lintang.multiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.Screen
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.style.NavigationItemStyle
import com.lintang.multiplatform.util.Constants.COLLAPSE_PANEL_HEIGHT
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.lintang.multiplatform.util.Id
import com.lintang.multiplatform.util.Res
import com.lintang.multiplatform.util.logout
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel(onMenuClick: () -> Unit) {
    val breakPoint = rememberBreakpoint()
    // jika layar kecil tampilkan collapsed side panel
    if (breakPoint > Breakpoint.MD) {
        SidePanelInternal()
    } else {
        collapseSlideMenu(onMenuClick)
    }

}

@Composable
fun OverFlowSidePanel(onMenuClose: () -> Unit, content: @Composable () -> Unit) {
    val breakPoint = rememberBreakpoint()
    var translateX by remember { mutableStateOf((-100).percent) }
    var opacity by remember { mutableStateOf((0).percent) }
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()

    LaunchedEffect(key1 = breakPoint) {
        translateX = 0.percent
        opacity = 100.percent
        if (breakPoint > Breakpoint.MD) {
            scope.launch {
                translateX = (-100).percent
                opacity = 0.percent
                delay(500)
                onMenuClose()
            }
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(100.vh)
            .opacity(opacity)
            .position(position = Position.Fixed).zIndex(9)
            .transition(CSSTransition("opacity", duration = 300.ms))
            .background(Theme.HalfBlack.rgb)
    ) {
        Column(
            Modifier.padding(all = 24.px)
                .fillMaxHeight()
                .translateX(translateX)
                .width(if (breakPoint < Breakpoint.MD) 50.percent else 25.percent)
                .transition(CSSTransition("translate", duration = 300.ms))
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .background(Theme.Secondary.rgb)
        ) {
            Row(
                modifier = Modifier.margin(bottom = 60.px, top = 24.px),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FaXmark(
                    modifier = Modifier
                        .margin(right = 20.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick {
                            scope.launch {
                                translateX = (-100).percent
                                opacity = 0.percent
                                delay(500)
                                onMenuClose()
                            }
                        },
                    size = IconSize.XL
                )
                Image(
                    modifier = Modifier.width(80.px).cursor(Cursor.Pointer)
                        .onClick {
                            context.router.navigateTo(Screen.Home.route)
                        },
                    src = Res.Image.logo,
                    description = "logo image"
                )
            }
            content()
        }
    }

}

@Composable
private fun SidePanelInternal() {
    val context = rememberPageContext()

    Column(
        modifier = Modifier.padding(leftRight = 40.px, top = 50.px)
            .width(SIDE_PANEL_WIDTH.px)
            .height(100.vh) // agar full height
            .position(Position.Fixed)
            .background(Theme.Secondary.rgb)
            .zIndex(9) // urutan agar paling bawah dalam stack
    ) {
        Image(
            modifier = Modifier
                .margin(bottom = 60.px)
                .cursor(Cursor.Pointer)
                .onClick {
                    context.router.navigateTo(Screen.Home.route)
                },
            src = Res.Image.logo,
            description = "Logo Image"
        )
        SpanText(
            modifier = Modifier.fontFamily(FONT_FAMILY)
                .margin(bottom = 30.px)
                .fontSize(14.px)
                .color(Theme.HalfWhite.rgb),
            text = "Dashboard"
        )
        NavigationItems()
    }

}

@Composable
private fun NavigationItem(
    modifier: Modifier,
    selected: Boolean = false,
    title: String,
    icon: String,
    onClick: () -> Unit
) {
    Row(
        NavigationItemStyle.toModifier().then(
            modifier
        ).cursor(Cursor.Pointer).onClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        VectorIcon(
            modifier = Modifier.margin(right = 10.px),
            selected = selected,
            pathData = icon,
        )
        SpanText(
            modifier = Modifier
                .id(Id.navigationTitle)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition = selected,
                    other = Modifier.color(Theme.Primary.rgb)
                ), // agar hover terlihat
            text = title
        )
    }
}

@Composable
 fun NavigationItems() {
    val page = rememberPageContext()
    NavigationItem(
        modifier = Modifier.padding(bottom = 24.px),
        selected = page.route.path == Screen.AdminHome.route,
        title = "Home",
        icon = Res.PathIcon.home,
        onClick = {
            page.router.navigateTo(Screen.AdminHome.route)
        }
    )
    NavigationItem(
        modifier = Modifier.padding(bottom = 24.px),
        selected = page.route.path == Screen.AdminCreate.route,
        title = "Create Post",
        icon = Res.PathIcon.create,
        onClick = {
            page.router.navigateTo(Screen.AdminCreate.route)
        }
    )
    NavigationItem(
        modifier = Modifier.padding(bottom = 24.px),
        selected = page.route.path == Screen.AdminMyPosts.route,
        title = "My Posts",
        icon = Res.PathIcon.posts,
        onClick = {
            page.router.navigateTo(Screen.AdminMyPosts.route)
        }
    )

    NavigationItem(
        modifier = Modifier.padding(bottom = 24.px),
        selected = false,
        title = "Logout",
        icon = Res.PathIcon.logout,
        onClick = {
            logout()
            page.router.navigateTo(Screen.AdminLogin.route)
        })
}

@Composable
private fun VectorIcon(
    modifier: Modifier,
    selected: Boolean,
    pathData: String
) {
    Svg(attrs = modifier.id(Id.svgParent).width(24.px).height(24.px).toAttrs {
        attr("viewBox", value = "0 0 24 24")
        attr("fill", value = "none")
    }) {
        Path(
            attrs = Modifier.id(Id.vectorIcon)
                .thenIf(condition = selected, other = Modifier.styleModifier {
                    property("stroke", Theme.Primary.hex)
                }).toAttrs {
                    attr("d", pathData)

                    attr("stroke-width", "2")
                    attr("stroke-linecap", "round")
                    attr("stroke-linejoin", "round")
                })
    }
}

@Composable
private fun collapseSlideMenu(onMenuClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(COLLAPSE_PANEL_HEIGHT.px)
            .padding(leftRight = 24.px)
            .background(Theme.Secondary.rgb),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FaBars(
            modifier = Modifier
                .margin(right = 24.px)
                .color(Colors.White)
                .cursor(Cursor.Pointer)
                .onClick { onMenuClick() },
            size = IconSize.XL
        )
        Image(
            modifier = Modifier.width(80.px),
            src = Res.Image.logo,
            description = "image logo"
        )
    }
}

