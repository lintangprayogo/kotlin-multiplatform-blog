package com.lintang.multiplatform.models

import kotlinx.serialization.Serializable

@Serializable
actual data class Post(
    actual val _id: String="",
    actual val author: String,
    actual val date: Long,
    actual val title: String,
    actual val subtitle: String,
    actual val thumbnail: String,
    actual val content: String,
    actual val category: Category,
    actual val isPopular: Boolean = false,
    actual val isMain: Boolean = false,
    actual val isSponsored: Boolean = false,
)
