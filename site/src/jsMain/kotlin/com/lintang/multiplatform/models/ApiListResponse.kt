package com.lintang.multiplatform.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
actual  sealed class ApiListResponse {
    @Serializable
    @SerialName("idle")
    actual object Idle:ApiListResponse()
    @Serializable
    @SerialName("error")
    actual data class Error(val message:String):ApiListResponse()
    @Serializable
    @SerialName("Success")
    actual data class Success(val data:List<PostWithoutDetails>):ApiListResponse()
}