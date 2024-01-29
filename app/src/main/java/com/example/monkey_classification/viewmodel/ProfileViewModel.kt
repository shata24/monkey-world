package com.example.monkey_classification.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monkey_classification.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {

    val db = Firebase.firestore

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    @RequiresApi(Build.VERSION_CODES.O)
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