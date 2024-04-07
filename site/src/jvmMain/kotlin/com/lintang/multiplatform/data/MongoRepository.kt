package com.lintang.multiplatform.data

import com.lintang.shared.Category
import com.lintang.multiplatform.models.NewsLater
import com.lintang.multiplatform.models.Post
import com.lintang.multiplatform.models.PostWithoutDetails
import com.lintang.multiplatform.models.User

interface MongoRepository {
     suspend fun  checkIfUserExist(user: User):User?
     suspend fun checkUserId(id:String):Boolean
     suspend fun addPost(post: Post):Boolean
     suspend fun getMyPosts(skip: Int, author: String): List<PostWithoutDetails>
     suspend fun getMainPosts(): List<PostWithoutDetails>
     suspend fun getLastestPosts(skip: Int): List<PostWithoutDetails>
     suspend fun getPopularPosts(skip: Int): List<PostWithoutDetails>
     suspend fun searchPostByTitle(skip: Int, title: String): List<PostWithoutDetails>
     suspend fun searchPostByCategory(skip: Int, category: com.lintang.shared.Category): List<PostWithoutDetails>
     suspend fun getPosById(id: String): Post?
     suspend fun deleteSelectedPosts(postIds: List<String>): Boolean
    suspend fun updatePost(post: Post): Boolean
    suspend fun getSponsoredPosts(): List<PostWithoutDetails>
    suspend fun subscribe(newsLater: NewsLater):String
}