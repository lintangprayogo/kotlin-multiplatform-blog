package com.lintang.multiplatform.models

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(color:String) {
    Technology(Theme.Green.hex),
    Programing(Theme.Yellow.hex),
    Design(Theme.Purple.hex),
}