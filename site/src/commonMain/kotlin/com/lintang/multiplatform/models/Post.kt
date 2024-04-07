package com.lintang.multiplatform.models

import com.lintang.shared.Category
import kotlinx.serialization.Serializable


@Serializable
data class Post(
    val _id: String = "",
    val author: String,
    val date: Long,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val content: String,
    val category: com.lintang.shared.Category,
    val isPopular: Boolean = false,
    val isMain: Boolean = false,
    val isSponsored: Boolean = false,
)

