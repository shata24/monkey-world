@file:JvmName("HomeScreenKt")

package com.example.monkey_classification.presentation.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.monkey_classification.R
import com.example.monkey_classification.model.Post
import com.example.monkey_classification.model.User
import com.example.monkey_classification.presentation.screenItem.ScreenItem
import com.example.monkey_classification.ui.theme.primaryColor
import com.example.monkey_classification.viewmodel.HomeViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    auth: FirebaseAuth,
    viewModel: HomeViewModel = viewModel(),
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val posts by viewModel.posts.observeAsState()
    val user by viewModel.user.observeAsState()
    val context = LocalContext.current

    if(posts != null && user != null) {
        isLoading = false
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadAllPosts(context)
        viewModel.getUser(auth.uid ?: "")
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                title = "Home"
            )
        },
        topBar = {
            AppBar(navController = navController, title = "Home")
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                posts?.let { postList ->
                    items(postList) {post ->
                        PostBlock(post = post, navController = navController, viewModel = viewModel, user =  user, context = context)
                    }
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    navController.navigate(ScreenItem.AddPostScreenItem.route)
                },
                content = { Icon(Icons.Filled.Add, contentDescription = "Add") }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostBlock(
    post: Post,
    navController: NavController,
    viewModel: HomeViewModel,
    user: User?,
    context: Context
) {
    Spacer(modifier = Modifier.height(8.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0EEEE))
            .padding(16.dp)
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("postId", post.id)
                navController.navigate(ScreenItem.PostDetailsScreenItem.route)
            }
    ) {
        // User Info and Post Content
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            SubcomposeAsyncImage(
                model = post.image,
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(35.dp),
                            color = Color.Black
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(id = R.drawable.avartar),
                            contentDescription = "profile image"
                        )
                    }
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // User Name and Post Text
            Column {
                Text(post.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                val formattedDateTime = convertTimestampToDateTime(post.time)
                Text(formattedDateTime, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = post.description)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (user?.email in post.likes) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier.clickable {
                            viewModel.removeLike(postId = post.id, userEmail = user?.email ?: "", context = context)
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            viewModel.addLike(postId = post.id, userEmail = user?.email ?: "", context = context)
                        }
                    )
                }

                val text = if (post.likes.size > 1) "Likes" else "Like"
                Text("${post.likes.size} $text")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (user?.email in post.dislikes) {
                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier.clickable {
                            viewModel.removeDislike(postId = post.id, userEmail = user?.email ?: "", context = context)
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            viewModel.addDislike(postId = post.id, userEmail = user?.email ?: "", context = context)
                        }
                    )
                }

                val text = if (post.dislikes.size > 1) "Dislikes" else "Dislike"
                Text("${post.dislikes.size} $text")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                val text = if (post.comments.size > 1) "Comments" else "Comment"
                Text("${post.comments.size} $text")
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

fun convertTimestampToDateTime(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
    return sdf.format(date)
}
