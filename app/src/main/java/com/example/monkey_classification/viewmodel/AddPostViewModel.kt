package com.example.monkey_classification.viewmodel

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.monkey_classification.model.Post
import com.example.monkey_classification.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AddPostViewModel : ViewModel() {
    val db = Firebase.firestore

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    @RequiresApi(Build.VERSION_CODES.O)
    fun addPost(post: Post, context: Context, navController: NavController) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                Toast.makeText(context, "Your status has been posted", Toast.LENGTH_SHORT)
                    .show()
                navController.popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun getUser(userId: String) = viewModelScope.launch {
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)!!
                    _user.value = user
                }
            }
    }

}