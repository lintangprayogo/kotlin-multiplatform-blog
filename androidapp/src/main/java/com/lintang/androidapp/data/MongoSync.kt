package com.lintang.androidapp.data

import com.lintang.androidapp.model.Post
import com.lintang.androidapp.util.Constants.APP_ID
import com.lintang.androidapp.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object MongoSync : MongoSyncRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config =
                SyncConfiguration.Builder(user, setOf(Post::class)).initialSubscriptions {
                    add(
                        query = it.query(Post::class),
                        name = "Blog Post"
                    )
                }
                    .log(LogLevel.ALL)
                    .build()

            realm = Realm.open(config)
        }
    }

    override fun readAllPosts(): Flow<RequestState<List<Post>>> {
        return if (user != null) {
            try {

                realm.query(Post::class).asFlow().map { result ->
                    RequestState.Success(data = result.list)
                }

            } catch (e: Exception) {
                flow { emit(RequestState.Error(Throwable(e.message))) }

            }

        } else {
            flow { emit(RequestState.Error(Throwable("Something bad happen"))) }
        }
    }

    override fun searchByTitle(query: String): Flow<RequestState<List<Post>>> {
        return if (user != null) {
            try {
                realm.query<Post>(query = "title CONTAINS[c] $0", query)
                    .asFlow().map { result ->
                    RequestState.Success(data = result.list)
                }

            } catch (e: Exception) {
                flow { emit(RequestState.Error(Throwable(e.message))) }

            }

        } else {
            flow { emit(RequestState.Error(Throwable("Something bad happen"))) }
        }
    }

    override fun searchByCategory(category: String): Flow<RequestState<List<Post>>> {
        return if (user != null) {
            try {
                realm.query<Post>(query = "category == $0", category).asFlow().map { result ->
                    RequestState.Success(data = result.list)
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(Throwable(e.message))) }
            }
        } else {
            flow { emit(RequestState.Error(Throwable("Something bad happen"))) }
        }
    }
}