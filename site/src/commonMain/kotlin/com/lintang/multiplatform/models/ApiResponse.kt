package com.lintang.multiplatform.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(ApiResponseSerializer::class)
sealed class ApiResponse {
    @Serializable
    @SerialName("idle")
    data object Idle : ApiResponse()

    @Serializable
    @SerialName("success")
    data class Success(val data: Post) : ApiResponse()

    @Serializable
    @SerialName("error")
    data class Error(val message: String) : ApiResponse()
}

// gunakan ini agar jvm tidak bingung  serializable mana digunakan
object ApiResponseSerializer :
    JsonContentPolymorphicSerializer<ApiResponse>(ApiResponse::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        "data" in element.jsonObject -> ApiResponse.Success.serializer()
        "message" in element.jsonObject -> ApiResponse.Error.serializer()
        else -> ApiResponse.Idle.serializer()
    }
}