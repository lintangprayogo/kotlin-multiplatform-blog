package com.lintang.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lintang.androidapp.screen.category.CategoryScreen
import com.lintang.androidapp.screen.category.CategoryViewModel
import com.lintang.androidapp.screen.detail.DetailScreen
import com.lintang.androidapp.screen.home.HomeScreen
import com.lintang.androidapp.screen.home.HomeViewModel
import com.lintang.androidapp.util.Constants
import com.lintang.androidapp.util.Constants.CATEGORY_ARGUMENT
import com.lintang.androidapp.util.Constants.POST_ID_ARGUMENT
import com.lintang.shared.Category

@Composable
fun setUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
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
                    if(!it){
                        query = ""
                        active =false
                        viewModel.resetSearch()
                    }
                },
                onCategoryClick = {
                    navController.navigate(Screen.Category.passCategory(it.name))
                },
                onPostClick = {
                    navController.navigate(Screen.Detail.passId(it._id))
                }
            )
        }
        composable(
            Screen.Category.route,
            arguments = listOf(navArgument(name = Constants.CATEGORY_ARGUMENT) {
                type = NavType.StringType
            })

        ) {
            val viewModel: CategoryViewModel = viewModel()
            val category =
                it.arguments?.getString(CATEGORY_ARGUMENT) ?: com.lintang.shared.Category.Programming.name

            CategoryScreen(
                category = com.lintang.shared.Category.valueOf(category),
                posts = viewModel.categoryPosts.value,
                onBack = {
                    navController.popBackStack()
                },
                onPostClick = {
                    navController.navigate(Screen.Detail.passId(it._id))
                }
            )
        }
        composable(
            Screen.Detail.route,
            arguments = listOf(navArgument(POST_ID_ARGUMENT) {
                type = NavType.StringType
            })
        ) {
            val postId = it.arguments?.getString(POST_ID_ARGUMENT)
            DetailScreen(url = "http://10.0.2.2:8080/posts/post?${POST_ID_ARGUMENT}=$postId") {
                navController.popBackStack()
            }
        }
    }
}