package com.lintang.androidapp.data

import com.lintang.androidapp.model.Post
import com.lintang.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoSyncRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<Post>>>
    fun searchByTitle(title: String): Flow<RequestState<List<Post>>>
}