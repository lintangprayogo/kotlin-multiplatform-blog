package com.lintang.androidapp.navigation

import androidx.compose.runtime.Composable
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
            HomeScreen(viewModel.allPosts.value)
        }
        composable(Screen.CategoryScreen.route) {
            it.arguments?.getString("category")
        }
        composable(Screen.CategoryScreen.route) {

        }
    }
}