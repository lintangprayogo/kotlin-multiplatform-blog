package com.lintang.shared.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lintang.shared.Screen
import com.lintang.shared.components.CategoryNavigationItems
import com.lintang.shared.components.SearchBar
import com.lintang.shared.models.Theme
import com.lintang.shared.util.Constants.HEADER_HEIGHT
import com.lintang.shared.util.Constants.PAGE_WIDTH
import com.lintang.shared.util.Id
import com.lintang.shared.util.Res
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import kotlinx.browser.document
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement
import com.lintang.shared.Category
@Composable
fun HeaderSection(
    breakpoint: Breakpoint,
    logo: String = Res.Image.logo,
    selectedCategory: Category? = null,
    onMenuOpen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.Secondary.rgb)
                .maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Header(
                breakpoint = breakpoint,
                selectedCategory = selectedCategory,
                logo = logo,
                onMenuOpen = onMenuOpen,
            )
        }
    }
}

@Composable
fun Header(
    breakpoint: Breakpoint,
    logo: String,
    selectedCategory: Category?,
    onMenuOpen: () -> Unit
) {
    var fullSearchBarOpened by remember { mutableStateOf(false) }
    val context = rememberPageContext()

    Row(
        modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .height(
                HEADER_HEIGHT.px
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (breakpoint <= Breakpoint.MD) {
            if (fullSearchBarOpened) {
                FaXmark(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick { fullSearchBarOpened = false },
                    size = IconSize.XL
                )
            } else {
                FaBars(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick { onMenuOpen() },
                    size = IconSize.XL
                )

            }

        }

        if (!fullSearchBarOpened) {
            Image(
                modifier = Modifier.margin(right = 50.px)
                    .width(if (breakpoint >= Breakpoint.SM) 100.px else 70.px)
                    .cursor(Cursor.Pointer)
                    .onClick {
                        context.router.navigateTo(Screen.Home.route)
                    },
                src = logo
            )
        }

        if (breakpoint >= Breakpoint.LG) {
            CategoryNavigationItems(false, selectedCategory )
        }

        Spacer()
        SearchBar(
            breakpoint = breakpoint,
            fullWidth = fullSearchBarOpened,
            onEnterClick = {
                val title = (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value
                context.router.navigateTo(Screen.SearchPage.searchPostByTitle(title))
            }, onSearchIconClick = {
                fullSearchBarOpened = it
            },
            darkTheme = true
        )
    }
}