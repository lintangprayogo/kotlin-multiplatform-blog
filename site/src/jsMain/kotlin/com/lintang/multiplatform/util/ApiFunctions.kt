package com.lintang.multiplatform.util

import com.lintang.shared.Category
import com.lintang.multiplatform.models.ApiListResponse
import com.lintang.multiplatform.models.ApiResponse
import com.lintang.multiplatform.models.Constants.AUTHOR_PARAM
import com.lintang.multiplatform.models.Constants.CATEGORY_PARAM
import com.lintang.multiplatform.models.Constants.POST_ID_PARAM
import com.lintang.multiplatform.models.Constants.SKIP_PARAM
import com.lintang.multiplatform.models.Constants.TITLE_PARAM
import com.lintang.multiplatform.models.NewsLater
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
            apiPath = "getmyposts?$SKIP_PARAM=$skip&$AUTHOR_PARAM=${localStorage["username"]}",
        )?.decodeToString()?.parseData<ApiListResponse>() ?: ApiListResponse.Error("Not Found")
        onSuccess(result)
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun getMainPost(
    onSuccess: (response: ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {

    try {
        val result = window.api.tryPost(
            apiPath = "getmainposts",
        )?.decodeToString()?.parseData<ApiListResponse>() ?: ApiListResponse.Error("Not Found")
        onSuccess(result)
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun getLastestPost(
    skip: Int,
    onSuccess: (response: ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {

    try {
        val result = window.api.tryGet(
            apiPath = "getlatestposts?skip=$skip",
        )?.decodeToString()?.parseData<ApiListResponse>() ?: ApiListResponse.Error("Not Found")
        onSuccess(result)
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}
suspend fun getPopularPost(
    skip: Int,
    onSuccess: (response: ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {

    try {
        val result = window.api.tryGet(
            apiPath = "getpopularposts?skip=$skip",
        )?.decodeToString()?.parseData<ApiListResponse>() ?: ApiListResponse.Error("Not Found")
        onSuccess(result)
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun getSponsoredPost(
    onSuccess: (response: ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {

    try {
        val result = window.api.tryGet(
            apiPath = "getsponsoredposts",
        )?.decodeToString()?.parseData<ApiListResponse>() ?: ApiListResponse.Error("Not Found")
        onSuccess(result)
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun searchPostByTitle(
    title: String,
    skip: Int, onSuccess: (response: ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    return try {
        val result = window.api.tryGet(
            "searchposts?$SKIP_PARAM=$skip&$TITLE_PARAM=$title",
        )?.decodeToString()?.parseData<ApiListResponse>() ?: ApiListResponse.Error("Not Found")
        onSuccess(result)
    } catch (e: Exception) {
        onError(e)
    }
}

suspend fun searchPostByCategory(
    category: Category,
    skip: Int,
    onSuccess: (response: ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    return try {
        val result = window.api.tryGet(
            apiPath = "searchpostsbycategory?$SKIP_PARAM=$skip&$CATEGORY_PARAM=$category"
        )?.decodeToString().parseData<ApiListResponse>()

        onSuccess(result)
    } catch (e: Exception) {
        onError(e)
    }
}



suspend fun deleteSelectedPost(selectedPosts: List<String>): Boolean {
    return try {
        val result = window.api.tryPost(
            "deletemyposts",
            body = Json.encodeToString(selectedPosts.toList()).encodeToByteArray()
        )?.decodeToString()
        println(result)
        return result.toBoolean()
    } catch (e: Exception) {
        println("ERROR ${e.message} unknown error")
        false
    }

}


suspend fun getPostById(id: String): ApiResponse {
    return try {
        val result = window.api.tryPost(
            "getpostbyid?$POST_ID_PARAM=$id",
        )?.decodeToString()
        result.parseData()
    } catch (e: Exception) {
        ApiResponse.Error(e.message ?: "")
    }

}

suspend fun subscribeNews(newsLater: NewsLater): String {
    return try {
        val body = Json.encodeToString(newsLater).encodeToByteArray()
        val result = window.api.tryPost("subscribenews", body = body)
        return result?.decodeToString().parseData<String>().replace("\"\"","")
    } catch (e: Exception) {
        e.message ?: ""
    }
}

inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString(this.toString())
}