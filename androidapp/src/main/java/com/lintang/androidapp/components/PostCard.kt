package com.lintang.androidapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lintang.androidapp.model.Post
import com.lintang.androidapp.util.RequestState
import com.lintang.androidapp.util.convertLongToDate
import com.lintang.androidapp.util.decodeThumbnailImage
import com.lintang.shared.Category

@Composable
fun PostCard(post: Post, onPostClick: (post: Post) -> Unit) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable { onPostClick(post) },
        tonalElevation = 1.dp
    ) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                modifier = Modifier.height(260.dp),
                model = ImageRequest.Builder(context = context)
                    .data(
                        if (post.thumbnail.contains("http")) post.thumbnail
                        else post.thumbnail.decodeThumbnailImage()
                    ).build(),
                contentDescription = "Post Thumbnail",
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .alpha(0.5f),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = post.date.convertLongToDate()
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 6.dp),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    text = post.title
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 6.dp),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    text = post.subtitle
                )
                SuggestionChip(onClick = { }, label = {
                    Text(
                        text =Category.valueOf(post.category).name
                    )
                })
            }
        }


    }
}

@Composable
fun PostPreview(posts: RequestState<List<Post>>) {
    if (posts is RequestState.Success) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = posts.data, key = { post: Post -> post._id }) { post ->
                PostCard(post = post) {

                }
            }
        }
    } else if (posts is RequestState.Error) {
        Text(text = posts.throwable.message ?: "-", color = Color.Red, fontSize = 20.sp)
    }
}