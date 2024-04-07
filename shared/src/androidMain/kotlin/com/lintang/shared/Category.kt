package com.lintang.shared

import com.lintang.shared.CategoryColor
import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(override val color: String): CategoryColor {
    Technology(color = ""),
    Programming(color = ""),
    Design(color = "")
}
