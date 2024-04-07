package com.lintang.multiplatform.models

import com.lintang.shared.Category
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
    val category: com.lintang.shared.Category = com.lintang.shared.Category.Technology,
    val isPopular: Boolean = false,
    val isMain: Boolean = false,
    val isSponsored: Boolean = false,
)

