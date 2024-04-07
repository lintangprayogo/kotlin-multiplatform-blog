package com.lintang.androidapp.navigation

import com.lintang.androidapp.util.Constants


sealed class Screen(val route: String) {
    data object Home : Screen(route = "home_screen")
    data object Category : Screen(route = "category_screen/{${Constants.CATEGORY_ARGUMENT}}") {
        fun passCategory(category: String): String = "category_screen/${category}"
    }

    data object Detail : Screen(route = "detail_screen/{${Constants.POST_ID_ARGUMENT}}") {
        fun passId(id: String): String = "detail_screen/$id"
    }
}