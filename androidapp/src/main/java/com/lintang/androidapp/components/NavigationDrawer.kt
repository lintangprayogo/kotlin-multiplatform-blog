package com.lintang.androidapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lintang.androidapp.R
import com.lintang.shared.Category


@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onCategoryClick: (Category) -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 100.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "My Logo"
                    )

                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                        .padding(start = 20.dp, top = 12.dp),
                    text = "Categories"
                )
                Category.entries.forEach { category ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                text = category.name,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = false,
                        onClick = { onCategoryClick(category) })
                }
            }
        },
        content = content
    )
}