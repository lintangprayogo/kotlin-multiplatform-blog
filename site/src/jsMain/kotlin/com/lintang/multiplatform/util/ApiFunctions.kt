package com.lintang.multiplatform.util

import com.lintang.multiplatform.models.ApiListResponse
import com.lintang.multiplatform.models.Post
import com.lintang.multiplatform.models.RandomJoke
import com.lintang.multiplatform.models.User
import com.lintang.multiplatform.models.UserWithoutPassword
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.compose.http.http
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

suspend fun checkIfUserExist(user: User): UserWithoutPassword? {
    return try {
        val result = window.api.tryPost(
            apiPath = "usercheck",
            body = Json.encodeToString(user).encodeToByteArray()
        )
        result?.decodeToString().parseData<UserWithoutPassword>()

    } catch (e: Exception) {
        println("error ${e.message}")
        null
    }
}

suspend fun checkUserId(id: String): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "checkuserid",
            body = Json.encodeToString(id).encodeToByteArray()
        )?.decodeToString().parseData<Boolean>()
    } catch (e: Exception) {
        println("error ${e.message}")
        false
    }
}

suspend fun addPost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "addpost",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().parseData<Boolean>()
    } catch (e: Exception) {
        println("error ${e.message}")
        false
    }
}


suspend fun getRandomJoke(onComplete: (RandomJoke) -> Unit) {
    val date = localStorage["date"]
    val result = if (date != null) {
        val difrence = Date.now() - date.toDouble()
        val dayHasPassed = difrence >= 86400000
        if (dayHasPassed) {
            try {
                val result = window.http.get(Constants.HOMOR_BASE_URL).decodeToString()
                localStorage["date"] = Date.now().toString()
                localStorage["joke"] = result
                result.parseData()
            } catch (e: Exception) {
                RandomJoke(-1, e.message.toString())
            }
        } else {
            try {
                localStorage["joke"].parseData<RandomJoke>()
            } catch (e: Exception) {
                println("error ${e.message}")
                RandomJoke(-1, e.message.toString())
            }
        }
    } else {
        try {
            val result = window.http.get(Constants.HOMOR_BASE_URL).decodeToString()
            localStorage["date"] = Date.now().toString()
            localStorage["joke"] = result
            result.parseData()
        } catch (e: Exception) {
            println("error ${e.message}")
            RandomJoke(-1, e.message.toString())
        }
    }

    onComplete(result)
}

suspend fun getMyPost(
    skip: Int,
    onSuccess: (response: ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {

    try {
        val result = window.api.tryGet(
            apiPath = "getmyposts?skip=$skip&author=${localStorage["username"]}",
        )?.decodeToString()?.parseData<ApiListResponse>() ?: ApiListResponse.Error("Not Found")
        onSuccess(result)
    } catch (e: Exception) {
        onError(e)
    }
}

inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString(this.toString())
}