package com.lintang.multiplatform.api

import com.lintang.multiplatform.data.MongoDB
import com.lintang.multiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator

@Api(routeOverride = "addPost")
suspend fun AddPost(context: ApiContext) {
    try {
        val post = context.req.body?.decodeToString()?.let {
            Json.decodeFromString<Post>(it)
        }
        val newPost = post?.copy(_id = ObjectIdGenerator().generate().toString())

        val result = newPost?.let {
            context.data.getValue<MongoDB>().addPost(it).toString()
        } ?: false.toString()

        context.res.setBodyText(result)
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(e.message))

    }

}