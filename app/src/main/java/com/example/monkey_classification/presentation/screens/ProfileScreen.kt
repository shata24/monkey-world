package com.example.monkey_classification.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.monkey_classification.viewmodel.ProfileViewModel
import com.example.monkey_classification.R
import com.example.monkey_classification.model.User
import com.example.monkey_classification.presentation.screenItem.ScreenItem
import com.example.monkey_classification.ui.theme.primaryColor
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("RestrictedApi", "UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(navController: NavController, auth: FirebaseAuth) {

    val userId = auth.uid
    val profileViewModel = ProfileViewModel()

    var isLoading by rememberSaveable { mutableStateOf(true) }
    var _user by rememberSaveable { mutableStateOf(User()) }

    val user = profileViewModel.user.observeAsState()
    if(user.value != null) {
        isLoading = false
        _user = user.value!!
    }

    if(userId != null) {
        profileViewModel.getUser(userId)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, title = "Profile") },
        backgroundColor = Color.White,
        topBar = {
            AppBar(navController = navController, title = "Profile")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "back",
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                    Icon(
                        imageVector = Icons.Default.Edit, contentDescription = "update profile",
                        modifier = Modifier
                            .clickable {
                                navController.currentBackStackEntry?.savedStateHandle?.set("user", user.value)
                                navController.navigate(ScreenItem.UpdateProfileScreenItem.route)
                            }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 30.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = user.value?.image,
                        contentDescription = "profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
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

                    Spacer(modifier = Modifier.width(30.dp))

                    Column {
                        Text(
                            text = _user.name,
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .padding(start = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call, contentDescription = "",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Mobile: " + _user.mobileNumber,
                        style = MaterialTheme.typography.body1
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .padding(start = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mail, contentDescription = "",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Email: " + _user.email,
                        style = MaterialTheme.typography.body1
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .padding(start = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call, contentDescription = "",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Age: " + _user.age,
                        style = MaterialTheme.typography.body1
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .padding(start = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call, contentDescription = "",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Address: " + _user.address,
                        style = MaterialTheme.typography.body1
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .clickable {
                            auth.signOut()
                            navController.navigate(ScreenItem.IntroScreenItem.route) {
                                popUpTo(0)
                            }
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout, contentDescription = "",
                        tint = Color.Red,
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Log out",
                        color = Color.Red,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}