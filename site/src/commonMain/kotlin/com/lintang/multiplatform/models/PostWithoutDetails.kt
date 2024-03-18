package com.lintang.multiplatform.models

import kotlinx.serialization.Serializable

@Serializable
data class PostWithoutDetails(
    val _id: String = "",
    val author: String,
    val date: Long,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val content: String,
    val category: Category = Category.Technology,
    val isPopular: Boolean = false,
    val isMain: Boolean = false,
    val isSponsored: Boolean = false,
)

