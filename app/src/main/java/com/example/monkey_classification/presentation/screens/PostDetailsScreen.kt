package com.example.monkey_classification.presentation.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.monkey_classification.R
import com.example.monkey_classification.model.Comment
import com.example.monkey_classification.model.Post
import com.example.monkey_classification.presentation.ratingbar.CustomRatingBar
import com.example.monkey_classification.presentation.ratingbar.RatingBarConfig
import com.example.monkey_classification.presentation.ratingbar.RatingBarStyle
import com.example.monkey_classification.ui.theme.primaryColor
import com.example.monkey_classification.viewmodel.PostDetailsViewModel
import com.google.firebase.auth.FirebaseAuth
import java.lang.Math.ceil
import java.lang.Math.floor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    navController: NavController,
    postId: String,
    viewModel: PostDetailsViewModel = viewModel(),
    auth: FirebaseAuth
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val post = viewModel.post.observeAsState()
    val user by viewModel.user.observeAsState()
    val context = LocalContext.current

    if(post.value != null && user != null) {
        isLoading = false
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadPost(postId, context)
        viewModel.getUser(auth.uid ?: "")
    }

    Scaffold(
        modifier = Modifier.background(Color(0xFFB0D5E9)),
        topBar = {
            AppBar(navController = navController, title = "Post Details")
        }
    ) { PaddingValues ->
        Box(modifier = Modifier.padding(PaddingValues)) {

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }

            post.value?.let { post ->
                Log.d("ekhane", post.toString())
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // User Info and Post Content
                    Column(modifier = Modifier
                        .background(Color(0xFFF0F0F0))
                        .fillMaxWidth()
                        .padding(16.dp))
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
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
                                        androidx.compose.material.CircularProgressIndicator(
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
                                androidx.compose.material.Text(
                                    post.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                val formattedDateTime = convertTimestampToDateTime(post.time)
                                Text(formattedDateTime, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = post.description)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val sumOfRatings = post.ratings.sumOf { it.rating.toDouble() }.toFloat()
                    val emails = post.ratings.map { it.email }

                    if (user?.email in emails) {
                        Text(text = "Rating count: ${post.ratings.size}")
                        Text(text = "Rating: ${sumOfRatings/post.ratings.size}")
                    } else {
                        var rating by remember { mutableStateOf(0.0f) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomRatingBar(
                                value = rating,
                                onValueChange = {
                                    rating = it
                                },
                                onRatingChanged = {
                                    Log.d("Rating Value", "RatingBarView: $it")
                                },
                                config = RatingBarConfig()
                                    .style(RatingBarStyle.HighLighted)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            androidx.compose.material.Button(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = primaryColor,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    viewModel.addRating(postId = postId, userEmail = user?.email ?: "", rating = rating, context = context)
                                },
                                modifier = Modifier.padding(vertical = 16.dp)
                            ) {
                                Text(text = "Submit", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Add Comment
                    var commentText by remember { mutableStateOf(TextFieldValue()) }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            OutlinedTextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                singleLine = true,
                                placeholder = { Text(text = "Add a comment") }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    // Add comment logic
                                    val newComment = commentText.text
                                    if (newComment.isNotEmpty()) {
                                        // add new comment
                                        viewModel.addCommentToPost(postId = post.id, comment = Comment(user?.name ?: "", newComment), context = context)
                                        commentText = TextFieldValue()
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = null)
                            }
                        }
                    }

                    // Comments
                    Text(
                        text = "Comments",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(post.comments) { comment ->
                            CommentItem(comment = comment)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF0F0F0))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = comment.name,
                style = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.body,
                style = LocalTextStyle.current.copy(fontSize = 16.sp)
            )
        }
    }
}