package com.lintang.multiplatform.models

import kotlinx.serialization.Serializable

@Serializable
data class RandomJoke(val id: Int, val joke: String)
