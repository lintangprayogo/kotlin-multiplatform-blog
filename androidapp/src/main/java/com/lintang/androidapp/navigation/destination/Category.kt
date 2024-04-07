package com.lintang.androidapp.navigation.destination

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lintang.androidapp.model.Post
import com.lintang.androidapp.navigation.Screen
import com.lintang.androidapp.screen.category.CategoryScreen
import com.lintang.androidapp.screen.category.CategoryViewModel
import com.lintang.androidapp.util.Constants
import com.lintang.shared.Category

fun NavGraphBuilder.categoryRoute(
    onPostClick: (Post) -> Unit,
    onBack: () -> Unit
) {
    composable(
        Screen.Category.route,
        arguments = listOf(navArgument(name = Constants.CATEGORY_ARGUMENT) {
            type = NavType.StringType
        })

    ) {
        val viewModel: CategoryViewModel = viewModel()
        val category =
            it.arguments?.getString(Constants.CATEGORY_ARGUMENT)
                ?: Category.Programming.name

        CategoryScreen(
            category = Category.valueOf(category),
            posts = viewModel.categoryPosts.value,
            onPostClick = onPostClick,
            onBack = onBack

        )
    }
}