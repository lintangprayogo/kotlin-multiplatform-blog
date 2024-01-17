package com.lintang.multiplatform

import com.lintang.multiplatform.util.Constants.TITLE_PARAM

sealed class Screen(val route: String) {
    object AdminHome : Screen(route = "/admin/")
    object AdminLogin : Screen(route = "/admin/login")
    object AdminCreate : Screen(route = "/admin/create")
    object AdminMyPosts : Screen(route = "/admin/myposts") {
        fun searchPost(title: String) = "$route?$TITLE_PARAM=$title"
    }

    object Success : Screen(route = "/admin/success")
    object HomePage : Screen(route = "/")

}