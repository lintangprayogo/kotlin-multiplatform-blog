package com.lintang.multiplatform.models



expect class Post {
    val _id: String
    val author: String
    val date: Long
    val title: String
    val subTitle: String
    val thumbnail: String
    val content: String
    val category: Category
    val isPopular: Boolean
    val isMain: Boolean
    val isSponsored: Boolean
}
