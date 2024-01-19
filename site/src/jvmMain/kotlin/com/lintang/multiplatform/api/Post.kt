package com.lintang.multiplatform.api

import com.lintang.multiplatform.data.MongoDB
import com.lintang.multiplatform.models.ApiListResponse
import com.lintang.multiplatform.models.ApiResponse
import com.lintang.multiplatform.models.Constants.AUTHOR_PARAM
import com.lintang.multiplatform.models.Constants.POST_ID_PARAM
import com.lintang.multiplatform.models.Constants.SKIP_PARAM
import com.lintang.multiplatform.models.Constants.TITLE_PARAM
import com.lintang.multiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.Request
import com.varabyte.kobweb.api.http.Response
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator

@Api(routeOverride = "addpost")
suspend fun AddPost(context: ApiContext) {
    try {
        val post = context.req.getBody<Post>()
        val newPost =
            post?.copy(_id = post._id.ifEmpty { ObjectIdGenerator().generate().toString() })

        val result = newPost?.let {
            if (post._id.isEmpty()) {
                context.data.getValue<MongoDB>().addPost(it)
            } else {
                context.data.getValue<MongoDB>().updatePost(post = post)
            }

        } ?: false

        context.res.setBody(result)
    } catch (e: Exception) {
        context.res.setBody(e.message)
    }

}

@Api(routeOverride = "getmyposts")
suspend fun getMyPost(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val author: String = context.req.params[AUTHOR_PARAM] ?: ""
        val result = context.data.getValue<MongoDB>().getMyPosts(skip, author)
        context.res.setBody(ApiListResponse.Success(result))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message ?: "unknown error"))
    }
}

@Api(routeOverride = "deletemyposts")
suspend fun deleteMyPost(context: ApiContext) {
    try {
        val postIds = context.req.getBody<List<String>>() ?: listOf()
        val result = context.data.getValue<MongoDB>().deleteSelectedPosts(postIds)
        context.res.setBody(result)
    } catch (e: Exception) {
        context.res.setBody(e.message ?: e.stackTrace ?: "UKNOWN ERROR")
    }
}

@Api(routeOverride = "searchposts")
suspend fun searchPostByTitle(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val title: String = context.req.params[TITLE_PARAM] ?: ""
        val result = context.data.getValue<MongoDB>().searchPostByTitle(title = title, skip = skip)
        context.res.setBody(ApiListResponse.Success(result))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message ?: "unknown error"))
    }
}

@Api(routeOverride = "getpostbyid")
suspend fun getPostById(context: ApiContext) {
    try {
        val postId = context.req.params[POST_ID_PARAM]
        val post = if (postId.isNullOrEmpty()) null else context.data.getValue<MongoDB>().getPosById(postId)

        if (post == null) {
            context.res.setBody(ApiResponse.Error("Data Tidak temukan"))
        } else {
            context.res.setBody(ApiResponse.Success(post))
        }
    } catch (e: Exception) {
        context.res.setBody(e.message ?: e.stackTrace ?: "Unknown Error")
    }
}

inline fun <reified T> Request.getBody(): T? {
    return body?.decodeToString()?.let { return Json.decodeFromString(it) }
}

inline fun <reified T> Response.setBody(data: T) {
    setBodyText(Json.encodeToString(data))

}