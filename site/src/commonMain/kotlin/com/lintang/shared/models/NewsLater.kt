package com.lintang.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class NewsLater(
    val _id: String = "",
    val email: String
)
