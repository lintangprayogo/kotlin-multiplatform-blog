package com.lintang.androidapp.navigation.destination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lintang.androidapp.model.Post
import com.lintang.androidapp.navigation.Screen
import com.lintang.androidapp.screen.home.HomeScreen
import com.lintang.androidapp.screen.home.HomeViewModel
import com.lintang.shared.Category

fun NavGraphBuilder.homeRoute(
    onCategoryClick: (Category) -> Unit,
    onPostClick: (Post) -> Unit,
) {
    composable(Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()

        var query by remember { mutableStateOf("") }
        var searchBarOpened by remember { mutableStateOf(false) }
        var active by remember { mutableStateOf(false) }
        HomeScreen(
            posts = viewModel.allPosts.value,
            searchPosts = viewModel.searchPosts.value,
            searchBarOpened = searchBarOpened,
            query = query,
            active = active,
            onActiveChange = {
                active = it
            },
            onQueryChange = {
                query = it
            },
            onSearch = {
                viewModel.searchByTitle(it)
            },
            onSearchBarChange = {
                searchBarOpened = it
                if (!it) {
                    query = ""
                    active = false
                    viewModel.resetSearch()
                }
            },
            onPostClick = onPostClick,
            onCategoryClick = onCategoryClick
        )
    }
}