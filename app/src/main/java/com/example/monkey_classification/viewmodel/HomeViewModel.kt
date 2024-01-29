package com.example.monkey_classification.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monkey_classification.model.Comment
import com.example.monkey_classification.model.Post
import com.example.monkey_classification.model.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAllPosts(context: Context) {
        val db = Firebase.firestore
        val loadedPosts = mutableListOf<Post>()

        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(context, "No Post found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                for (document in result) {
                    val postId = document.id
                    val description = document.getString("description") ?: ""
                    val name = document.getString("name") ?: ""
                    val image = document.getString("image") ?: ""
                    val time = document.getTimestamp("time") ?: Timestamp(Date())

                    val comments = document.get("comments") as? List<Map<String, String>> ?: emptyList()
                    val likes = document.get("likes") as? List<String> ?: emptyList()
                    val dislikes = document.get("dislikes") as? List<String> ?: emptyList()

                    val commentList = comments.map { comment ->
                        Comment(comment["name"] as String, comment["body"] as String)
                    }

                    val post = Post(
                        id = postId,
                        description = description,
                        name = name,
                        image = image,
                        time = time,
                        comments = commentList,
                        likes = likes,
                        dislikes = dislikes
                    )

                    loadedPosts.add(post)
                }
                _posts.value = loadedPosts
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something went wrong fetching data", Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addLike(postId: String, userEmail: String, context: Context) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.runBatch { batch ->
            // Add the user's email to the 'likes' field
            batch.update(
                firestore.collection("posts").document(postId),
                "likes", FieldValue.arrayUnion(userEmail)
            )

            // Remove the user's email from the 'dislikes' field
            batch.update(
                firestore.collection("posts").document(postId),
                "dislikes", FieldValue.arrayRemove(userEmail)
            )
        }
        loadAllPosts(context)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun addDislike(postId: String, userEmail: String, context: Context) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.runBatch { batch ->
            // Add the user's email to the 'likes' field
            batch.update(
                firestore.collection("posts").document(postId),
                "likes", FieldValue.arrayRemove(userEmail)
            )

            // Remove the user's email from the 'dislikes' field
            batch.update(
                firestore.collection("posts").document(postId),
                "dislikes", FieldValue.arrayUnion(userEmail)
            )
        }
        loadAllPosts(context)
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
    fun removeLike(postId: String, userEmail: String, context: Context) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.runBatch { batch ->
            batch.update(
                firestore.collection("posts").document(postId),
                "likes", FieldValue.arrayRemove(userEmail)
            )
        }
        loadAllPosts(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeDislike(postId: String, userEmail: String, context: Context) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.runBatch { batch ->
            batch.update(
                firestore.collection("posts").document(postId),
                "dislikes", FieldValue.arrayRemove(userEmail)
            )
        }
        loadAllPosts(context)
    }
}