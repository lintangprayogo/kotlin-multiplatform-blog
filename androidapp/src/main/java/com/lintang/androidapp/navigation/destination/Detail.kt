package com.lintang.androidapp.navigation.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lintang.androidapp.navigation.Screen
import com.lintang.androidapp.screen.detail.DetailScreen
import com.lintang.androidapp.util.Constants

fun NavGraphBuilder.detailRoute(onBack: () -> Unit) {
    composable(
        Screen.Detail.route,
        arguments = listOf(navArgument(Constants.POST_ID_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val postId = it.arguments?.getString(Constants.POST_ID_ARGUMENT)
        DetailScreen(url = "http://10.0.2.2:8080/posts/post?${Constants.POST_ID_ARGUMENT}=$postId&&${com.lintang.shared.Constants.SHOW_SECTIONS_PARAM}=false",onBack)
    }
}