package com.lintang.multiplatform

sealed class Screen(val route: String) {
    object AdminHome : Screen(route = "/admin/")
    object AdminLogin : Screen(route = "/admin/login")
    object AdminCreate : Screen(route = "/admin/create")
    object AdminMyPosts : Screen(route = "/admin/myposts")
    object Success : Screen(route = "/admin/success")
    object HomePage : Screen(route = "/")

}