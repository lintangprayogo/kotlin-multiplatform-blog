package com.lintang.androidapp.navigation



sealed class Screen(val route: String) {
    object HomeScreen : Screen(route = "home_screen")
    object CategoryScreen : Screen(route = "category_screen/{category}") {
        fun passCategory(category: String): String = "category_screen/${category}"
    }

    object DetailScreen : Screen(route = "detail_screen/{postId}") {
        fun passId(id: Long): String = "detail_screen/$id"
    }
}