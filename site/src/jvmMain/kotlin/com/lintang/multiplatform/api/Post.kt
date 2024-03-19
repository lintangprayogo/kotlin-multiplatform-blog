package com.lintang.multiplatform.api

import com.lintang.multiplatform.data.MongoDB
import com.lintang.multiplatform.models.ApiListResponse
import com.lintang.multiplatform.models.ApiResponse
import com.lintang.multiplatform.models.Category
import com.lintang.multiplatform.models.Constants.AUTHOR_PARAM
import com.lintang.multiplatform.models.Constants.CATEGORY_PARAM
import com.lintang.multiplatform.models.Constants.POST_ID_PARAM
import com.lintang.multiplatform.models.Constants.SKIP_PARAM
import com.lintang.multiplatform.models.Constants.TITLE_PARAM
import com.lintang.multiplatform.models.Post
import com.lintang.multiplatform.utils.getBody
import com.lintang.multiplatform.utils.setBody
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
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
suspend fun getMyPosts(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val author: String = context.req.params[AUTHOR_PARAM] ?: ""
        val result = context.data.getValue<MongoDB>().getMyPosts(skip, author)
        context.res.setBody(ApiListResponse.Success(result))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message ?: "unknown error"))
    }
}

@Api(routeOverride = "getmainposts")
suspend fun getMainPosts(context: ApiContext) {
    try {
        val result = context.data.getValue<MongoDB>().getMainPosts()
        context.res.setBody(ApiListResponse.Success(result))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message ?: "unknown error"))
    }
}

@Api(routeOverride = "getsponsoredposts")
suspend fun getSponsoredPosts(context: ApiContext) {
    try {
        val result = context.data.getValue<MongoDB>().getSponsoredPosts()
        context.res.setBody(ApiListResponse.Success(result))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message ?: "unknown error"))
    }
}
@Api(routeOverride = "getpopularposts")
suspend fun getPopularPosts(context: ApiContext) {
    try {
        val result = context.data.getValue<MongoDB>().getSponsoredPosts()
        context.res.setBody(ApiListResponse.Success(result))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message ?: "unknown error"))
    }
}
@Api(routeOverride = "getlatestposts")
suspend fun getLastestPosts(context: ApiContext) {
    try {
        val skip = (context.req.params["skip"]?:"0").toInt()
        val result = context.data.getValue<MongoDB>().getLastestPosts(skip)
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

@Api(routeOverride = "searchpostsbycategory")
suspend fun searchPostByCategory(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val category = Category.valueOf(context.req.params[CATEGORY_PARAM] ?: Category.Design.name)
        val result =
            context.data.getValue<MongoDB>().searchPostByCategory(skip = skip, category = category)
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

