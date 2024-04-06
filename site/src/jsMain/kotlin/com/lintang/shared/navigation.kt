package com.lintang.shared

import com.lintang.shared.models.Constants.CATEGORY_PARAM
import com.lintang.shared.models.Constants.POST_ID_PARAM
import com.lintang.shared.models.Constants.TITLE_PARAM
import com.lintang.shared.models.Constants.UPDATED_PARAM

sealed class Screen(val route: String) {
    data object Home : Screen(route = "/")
    data object AdminHome : Screen(route = "/admin/")
    data object AdminLogin : Screen(route = "/admin/login")
    data object AdminCreate : Screen(route = "/admin/create") {
        fun editPostId(postId: String) = "$route?$POST_ID_PARAM=$postId"
    }

    data object AdminMyPosts : Screen(route = "/admin/myposts") {
        fun searchPost(title: String) = "$route?$TITLE_PARAM=$title"

    }

    data object Success : Screen(route = "/admin/success") {
        fun updated() = "$route?$UPDATED_PARAM=true"
    }

    data object SearchPage : Screen(route = "/search/query") {
        fun searchPostByCategory(category: Category) = "$route?${CATEGORY_PARAM}=$category"
        fun searchPostByTitle(title: String) = "$route?${TITLE_PARAM}=$title"
    }


    data object PostPage : Screen(route = "/posts/post") {
        fun editPostId(postId: String) = "$route?$POST_ID_PARAM=$postId"
    }
}