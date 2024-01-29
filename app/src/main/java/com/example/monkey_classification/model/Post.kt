package com.example.monkey_classification.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.util.Date
import com.google.firebase.firestore.IgnoreExtraProperties

@RequiresApi(Build.VERSION_CODES.O)
data class Post(
    var id: String = "",
    val description: String = "",
    val name: String = "",
    val image: String = "",
    val time: Timestamp = Timestamp(Date()),
    val likes: List<String> = listOf(),
    val dislikes: List<String> = listOf(),
    val comments: List<Comment> = listOf(),
    val ratings: List<Rating> = listOf()
)

@IgnoreExtraProperties
data class Comment(
    val name: String,
    val body: String,
) {
    constructor() : this("", "")
}

@IgnoreExtraProperties
data class Rating(
    val email: String,
    val rating: Number
) {
    constructor() : this("", 0.0f)
}