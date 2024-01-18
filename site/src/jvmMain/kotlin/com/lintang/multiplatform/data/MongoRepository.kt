package com.lintang.multiplatform.data

import com.lintang.multiplatform.models.User

interface MongoRepository {
     suspend fun  checkIfUserExist(user: User):User?
     suspend fun checkUserId(id:String):Boolean
     suspend fun addPost(post:Post):Boolean
     suspend fun getMyPosts(skip: Int, author: String): List<PostWithoutDetails>
     suspend fun searchPostByTitle(skip: Int, title: String): List<PostWithoutDetails>
     suspend fun getPosById(id: String): Post?
     suspend fun deleteSelectedPosts(postIds: List<String>): Boolean
}