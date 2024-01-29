package com.example.monkey_classification.presentation.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.monkey_classification.model.Post
import com.example.monkey_classification.ui.theme.primaryColor
import com.example.monkey_classification.viewmodel.AddPostViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    viewModel: AddPostViewModel = viewModel(),
    navController: NavController,
    auth: FirebaseAuth
) {
    val user = viewModel.user.observeAsState()
    var isLoading by rememberSaveable { mutableStateOf(true) }

    if(user.value != null) {
        isLoading = false
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUser(auth.currentUser?.uid ?: "")
    }

    Scaffold(
        topBar = { AppBar(navController = navController, title = "Add Post") }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            var description by remember { mutableStateOf(TextFieldValue()) }
            val context = LocalContext.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }
                else {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 8.dp)
                    )

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = primaryColor,
                            contentColor = Color.White
                        ),
                        onClick = {
                            if (description.text != "") {
                                if (user.value != null) {
                                    viewModel.addPost(
                                        Post(description = description.text, name = user.value?.name ?: "", image = user.value?.image ?: ""),
                                        context,
                                        navController
                                    )
                                }
                                else {
                                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Post body cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Text(text = "Submit", color = Color.White)
                    }
                }
            }
        }
    }
}