package com.lintang.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lintang.androidapp.screen.home.HomeScreen
import com.lintang.androidapp.screen.home.HomeViewModel

@Composable
fun setUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
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
                    if(!it){
                        query = ""
                        active =false
                        viewModel.resetSearch()
                    }
                }
            )
        }
        composable(Screen.CategoryScreen.route) {
            it.arguments?.getString("category")
        }
        composable(Screen.CategoryScreen.route) {

        }
    }
}