package com.lintang.androidapp.screen.category

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.lintang.androidapp.components.PostPreview
import com.lintang.androidapp.model.Post
import com.lintang.androidapp.util.RequestState
import com.lintang.shared.Category

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryScreen(
    category: com.lintang.shared.Category,
    posts: RequestState<List<Post>>,
    onPostClick: (Post) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = category.name) }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "BACK ARROW"
                    )
                }
            })
        }
    ) {
        PostPreview(
            topMargin = it.calculateTopPadding(),
            posts = posts,
            onPostClick = onPostClick
        )
    }
}