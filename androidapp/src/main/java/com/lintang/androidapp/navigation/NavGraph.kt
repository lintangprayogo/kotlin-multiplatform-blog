package com.lintang.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lintang.androidapp.navigation.destination.categoryRoute
import com.lintang.androidapp.navigation.destination.detailRoute
import com.lintang.androidapp.navigation.destination.homeRoute

@Composable
fun SetUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        homeRoute(
            onCategoryClick = {
                navController.navigate(Screen.Category.passCategory(it.name))
            },
            onPostClick = {
                navController.navigate(Screen.Detail.passId(it._id))
            })
        categoryRoute(
            onBack = {
                navController.popBackStack()
            },
            onPostClick = { post ->
                navController.navigate(Screen.Detail.passId(post._id))
            }
        )
        detailRoute(onBack = {
            navController.popBackStack()
        })
    }
}