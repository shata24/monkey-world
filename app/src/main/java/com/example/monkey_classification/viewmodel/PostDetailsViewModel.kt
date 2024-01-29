package com.example.monkey_classification.viewmodel

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monkey_classification.model.Comment
import com.example.monkey_classification.model.Post
import com.example.monkey_classification.model.Rating
import com.example.monkey_classification.model.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Date

class PostDetailsViewModel : ViewModel() {

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCommentToPost(postId: String, comment: Comment, context: Context) {
        val fireStore = FirebaseFirestore.getInstance()

        fireStore.collection("posts").document(postId)
            .update("comments", FieldValue.arrayUnion(comment))
            .addOnSuccessListener {
                Toast.makeText(context, "Your comment has been posted successfully", Toast.LENGTH_SHORT).show()
                loadPost(postId, context)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPost(postId: String, context: Context) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("posts").document(postId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Process the document data
                    val description = document.getString("description") ?: ""
                    val name = document.getString("name") ?: ""
                    val image = document.getString("image") ?: ""
                    val time = document.getTimestamp("time") ?: Timestamp(Date())

                    val comments = document.get("comments") as? List<Map<String, String>> ?: emptyList()
                    val likes = document.get("likes") as? List<String> ?: emptyList()
                    val dislikes = document.get("dislikes") as? List<String> ?: emptyList()
                    val ratings = document.get("ratings") as? List<Map<String, Number>> ?: emptyList()

                    val commentList = comments.map { comment ->
                        Comment(comment["name"] as String, comment["body"] as String)
                    }

                    val ratingList = ratings.map { rating ->
                        Rating(rating["email"] as String, rating["rating"] as Number)
                    }

                    val postDetails = Post(
                        id = postId,
                        description = description,
                        name = name,
                        image = image,
                        time = time,
                        comments = commentList,
                        likes = likes,
                        dislikes = dislikes,
                        ratings = ratingList
                    )
                    _post.value = postDetails
                } else {
                    Toast.makeText(context, "Post not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUser(userId: String) = viewModelScope.launch {

        val docRef = Firebase.firestore.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    _user.value = user
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addRating(postId: String, userEmail: String, rating: Float, context: Context) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.runBatch { batch ->
            batch.update(
                firestore.collection("posts").document(postId),
                "ratings", FieldValue.arrayUnion(Rating(userEmail, rating))
            )
        }
        loadPost(postId, context)
    }
}