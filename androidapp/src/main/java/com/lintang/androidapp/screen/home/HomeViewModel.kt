package com.lintang.androidapp.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lintang.androidapp.data.MongoSync
import com.lintang.androidapp.model.Post
import com.lintang.androidapp.util.Constants
import com.lintang.androidapp.util.RequestState
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _allPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val allPosts: State<RequestState<List<Post>>> = _allPosts


    private val _searchPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val searchPosts: State<RequestState<List<Post>>> = _searchPosts
    init {
        viewModelScope.launch {
            App.create(Constants.APP_ID).login(Credentials.anonymous())
            fetchAllPost()
        }
    }

    private fun fetchAllPost() {
        viewModelScope.launch {
            MongoSync.readAllPosts().collectLatest {
                _allPosts.value = it
            }
        }
    }

    fun searchByTitle(title: String) {
        viewModelScope.launch {
            MongoSync.searchByTitle(title).collectLatest {
                _searchPosts.value = it
            }
        }
    }

    fun resetSearch() {
        _searchPosts.value = RequestState.Idle
    }
}