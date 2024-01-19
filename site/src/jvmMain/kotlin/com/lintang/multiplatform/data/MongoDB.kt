package com.lintang.multiplatform.data

import com.lintang.multiplatform.models.Constants.POST_PER_REQUEST
import com.lintang.multiplatform.models.Post
import com.lintang.multiplatform.models.PostWithoutDetails
import com.lintang.multiplatform.models.User
import com.lintang.multiplatform.utils.Constants
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes.descending
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList


@InitApi
fun initMongoDB(context: InitApiContext) {
    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    context.data.add(MongoDB(context))
}

class MongoDB(private val context: InitApiContext) : MongoRepository {
    private val client = MongoClient.create("mongodb://localhost:27017")
    private val database = client.getDatabase(Constants.DATABASE_NAME)
    private val userCollection = database.getCollection<User>("users")
    private val postCollection = database.getCollection<Post>("posts")

    override suspend fun checkIfUserExist(user: User): User? {
        return try {
            userCollection
                .find(
                    Filters.and(
                        Filters.eq(User::username.name, user.username),
                        Filters.eq(User::password.name, user.password)
                    )
                ).firstOrNull()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val count = userCollection.countDocuments(Filters.eq(User::_id.name, id))
            return count > 0
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun addPost(post: Post): Boolean {
        return try {
            postCollection.insertOne(post).wasAcknowledged()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            return false
        }
    }

    override suspend fun updatePost(post: Post): Boolean {
        return try {
            postCollection.updateOne(
                filter = Filters.eq(Post::_id.name, post._id), mutableListOf(
                    Updates.set(Post::title.name, post.title),
                    Updates.set(Post::subtitle.name, post.subtitle),
                    Updates.set(Post::category.name, post.category),
                    Updates.set(Post::thumbnail.name, post.thumbnail),
                    Updates.set(Post::content.name, post.content),
                    Updates.set(Post::isMain.name, post.isMain),
                    Updates.set(Post::isPopular.name, post.isPopular),
                    Updates.set(Post::isSponsored.name, post.isSponsored)
                )
            ).wasAcknowledged()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            return false
        }
    }

    override suspend fun getMyPosts(skip: Int, author: String): List<PostWithoutDetails> {
        return postCollection.withDocumentClass<PostWithoutDetails>()
            .find(Filters.eq(PostWithoutDetails::author.name, author))
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POST_PER_REQUEST)
            .toList()
    }

    override suspend fun deleteSelectedPosts(postIds: List<String>): Boolean {
        return postCollection.deleteMany(Filters.`in`(Post::_id.name, postIds)).wasAcknowledged()
    }

    override suspend fun searchPostByTitle(skip: Int, title: String): List<PostWithoutDetails> {
        val regexQuery = title.toRegex(RegexOption.IGNORE_CASE)
        return postCollection.withDocumentClass<PostWithoutDetails>()
            .find(Filters.regex(PostWithoutDetails::title.name, regexQuery.toPattern()))
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POST_PER_REQUEST)
            .toList()
    }

    override suspend fun getPosById(id: String): Post? {
        return postCollection.find(Filters.eq(Post::_id.name,id)).firstOrNull()
    }
}