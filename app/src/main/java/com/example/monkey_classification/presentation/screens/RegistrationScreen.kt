package com.example.monkey_classification.presentation.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.monkey_classification.model.User
import com.example.monkey_classification.ui.theme.primaryColor
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

inline fun Modifier.noRippleClickable(crossinline onClick: ()->Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegistrationScreen(navController: NavController, auth: FirebaseAuth) {

    val context = LocalContext.current
    var isLoading by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppBar(navController = navController, title = "Sign Up")
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if(isLoading) {
                Dialog(
                    onDismissRequest = {  },
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    Box(
                        contentAlignment= Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Please fill up this for creating an account.",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(vertical = 15.dp, horizontal = 25.dp)
                ) {

                    var name by rememberSaveable { mutableStateOf("") }
                    var userName by rememberSaveable { mutableStateOf("") }
                    var email by rememberSaveable { mutableStateOf("") }
                    var mobileNumber by rememberSaveable { mutableStateOf("") }
                    var password by rememberSaveable { mutableStateOf("") }
                    var age by rememberSaveable { mutableStateOf("") }
                    var address by rememberSaveable { mutableStateOf("") }
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }
                    val gender = remember { mutableStateOf("") }

                    Column {
                        TextField(
                            modifier = Modifier
                                .background(Color.White),
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Name") },
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        TextField(
                            modifier = Modifier
                                .background(Color.White),
                            value = userName,
                            onValueChange = { userName = it },
                            placeholder = { Text("Username") },
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        TextField(
                            modifier = Modifier
                                .background(Color.White),
                            value = mobileNumber,
                            onValueChange = { mobileNumber = it },
                            placeholder = { Text("Mobile") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        TextField(
                            modifier = Modifier
                                .background(Color.White),
                            value = age,
                            onValueChange = { age = it },
                            placeholder = { Text("Age") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        TextField(
                            modifier = Modifier
                                .background(Color.White),
                            value = address,
                            onValueChange = { address = it },
                            placeholder = { Text("Address") },
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))
                        GenderDropdown(gender)
                        TextField(
                            modifier = Modifier
                                .background(Color.White),
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("Email") },
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Gray
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        TextField(
                            modifier = Modifier
                                .background(Color.White),
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Password") },
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Gray
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff
                                val description = if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = {passwordVisible = !passwordVisible}){
                                    Icon(imageVector  = image, description)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .noRippleClickable() {
                                    isUsernameAvailable(userName) { isAvailable ->
                                        if (isAvailable) {
                                            if (
                                                name == "" || email == "" || password == "" || mobileNumber == ""
                                                || age == "" || address == "" || gender.value == ""
                                            ) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Fill up all fields",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                            else {
                                                isLoading = true
                                                auth
                                                    .createUserWithEmailAndPassword(
                                                        email.trim(),
                                                        password.trim()
                                                    )
                                                    .addOnCompleteListener() { task ->
                                                        if (task.isSuccessful) {
                                                            val authUser = task.result.user
                                                            authUser?.let {
                                                                it.sendEmailVerification()
                                                                val db = Firebase.firestore

                                                                val user = User(
                                                                    uid = it.uid,
                                                                    name = name,
                                                                    userName = userName,
                                                                    email = email,
                                                                    mobileNumber = mobileNumber,
                                                                    age = age,
                                                                    address = address,
                                                                    gender = gender.value
                                                                )
                                                                db
                                                                    .collection("users")
                                                                    .document(it.uid)
                                                                    .set(user)
                                                                    .addOnSuccessListener {
                                                                        isLoading = false
                                                                        Toast
                                                                            .makeText(
                                                                                context,
                                                                                "Registration is successful. Please verify your email.",
                                                                                Toast.LENGTH_SHORT
                                                                            )
                                                                            .show()
                                                                        navController.popBackStack()
                                                                    }
                                                                    .addOnFailureListener {
                                                                        Toast
                                                                            .makeText(
                                                                                context,
                                                                                "Something went wrong",
                                                                                Toast.LENGTH_SHORT
                                                                            )
                                                                            .show()
                                                                    }
                                                            }

                                                        } else {
                                                            isLoading = false
                                                            Toast
                                                                .makeText(
                                                                    context,
                                                                    task.exception?.message!!,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }
                                                    }
                                            }
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Username not available",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                                }
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(primaryColor)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "SIGN UP",
                                color = Color.White,
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenderDropdown(selectedOption: MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Other")

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOption.value = option
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }

        Text(text = "Selected Gender: ${selectedOption.value}")

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = primaryColor,
                contentColor = Color.White
            ),
            onClick = {
                expanded = !expanded
            }
        ) {
            Text(text = "Select Gender")
        }
    }
}

fun isUsernameAvailable(username: String, onComplete: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("users")

    val query = userCollection.whereEqualTo("userName", username).limit(1)
    query.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
        if (task.isSuccessful) {
            val querySnapshot = task.result
            val isAvailable = querySnapshot?.isEmpty ?: true
            onComplete(isAvailable)
        } else {
            // Handle query error
            onComplete(false)
        }
    }
}

