package com.example.monkey_classification

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monkey_classification.presentation.screens.LoginScreen
import com.example.monkey_classification.model.User
import com.example.monkey_classification.presentation.screenItem.ScreenItem
import com.example.monkey_classification.presentation.screens.AddPostScreen
import com.example.monkey_classification.presentation.screens.HomeScreen
import com.example.monkey_classification.presentation.screens.ImageClassificationScreen
import com.example.monkey_classification.presentation.screens.IntroScreen
import com.example.monkey_classification.presentation.screens.MonkeyDetailsScreen
import com.example.monkey_classification.presentation.screens.MonkeyListScreen
import com.example.monkey_classification.presentation.screens.PostDetailsScreen
import com.example.monkey_classification.presentation.screens.ProfileScreen
import com.example.monkey_classification.presentation.screens.RegistrationScreen
import com.example.monkey_classification.presentation.screens.SplashScreen
import com.example.monkey_classification.presentation.screens.UpdateProfileScreen
import com.example.monkey_classification.ui.theme.primaryColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val auth = FirebaseAuth.getInstance()

            SideEffect {
                systemUiController.setStatusBarColor(
                    color = primaryColor
                )
            }
            // A surface container using the 'background' color from the theme
            Surface(modifier = Modifier.fillMaxSize()) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenItem.SplashScreenItem.route
                ) {
                    composable(route = ScreenItem.HomeScreenItem.route) {
                        HomeScreen(navController = navController, auth = auth)
                    }
                    composable(route = ScreenItem.ProfileScreenItem.route) {
                        ProfileScreen(navController = navController, auth)
                    }
                    composable(route = ScreenItem.SplashScreenItem.route) {
                        SplashScreen(navController = navController, auth)
                    }
                    composable(route = ScreenItem.IntroScreenItem.route) {
                        IntroScreen(navController = navController)
                    }
                    composable(route = ScreenItem.RegistrationScreenItem.route) {
                        RegistrationScreen(navController = navController, auth)
                    }
                    composable(route = ScreenItem.LoginScreenItem.route) {
                        LoginScreen(navController = navController, auth)
                    }
                    composable(route = ScreenItem.UpdateProfileScreenItem.route) {
                        val userDetails = navController.previousBackStackEntry?.savedStateHandle?.get<User>("user")
                        userDetails?.let {
                            UpdateProfileScreen(navController = navController, auth, userDetails)
                        }
                    }
                    composable(route = ScreenItem.ImageClassificationScreenItem.route) {
                        ImageClassificationScreen(navController = navController, auth = auth)
                    }
                    composable(route = ScreenItem.AddPostScreenItem.route) {
                        AddPostScreen(navController = navController, auth = auth)
                    }
                    composable(route = ScreenItem.PostDetailsScreenItem.route) {
                        val postId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("postId")
                        postId?.let {
                            PostDetailsScreen(navController = navController, postId = postId, auth = auth)
                        }
                    }
                    composable(route = ScreenItem.MonkeyListScreenItem.route) {
                        MonkeyListScreen(navController = navController)
                    }
                    composable(route = ScreenItem.MonkeyDetailsScreenItem.route) {
                        val id = navController.previousBackStackEntry?.savedStateHandle?.get<String>("id")
                        id?.let {
                            MonkeyDetailsScreen(navController = navController, id)
                        }
                    }
                }
            }
        }
    }
}

