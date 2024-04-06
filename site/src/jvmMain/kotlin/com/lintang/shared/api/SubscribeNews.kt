package com.lintang.shared.api

import com.lintang.shared.data.MongoDB
import com.lintang.shared.models.NewsLater
import com.lintang.shared.utils.getBody
import com.lintang.shared.utils.setBody
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import org.bson.codecs.ObjectIdGenerator


@Api(routeOverride = "subscribenews")
suspend fun subscribeNews(context: ApiContext){
   val result= try {
        val newsLater =
            context.req.getBody<NewsLater>()?.copy(_id = ObjectIdGenerator().generate().toString())

        if (newsLater != null) {
             context.data.getValue<MongoDB>().subscribe(newsLater = newsLater)
        } else {
             "Theres is no data has been sent"
        }

    } catch (e: Exception) {
        "Something wen wrong ${e.message}"
    }
    context.res.setBody(result)
}