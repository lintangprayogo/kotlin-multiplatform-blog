package com.lintang.multiplatform.data

import com.lintang.multiplatform.models.Post
import com.lintang.multiplatform.models.User

interface MongoRepository {
     suspend fun  checkIfUserExist(user: User):User?
     suspend fun checkUserId(id:String):Boolean
     suspend fun addPost(post:Post):Boolean
}