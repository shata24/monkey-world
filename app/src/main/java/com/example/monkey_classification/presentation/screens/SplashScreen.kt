package com.example.monkey_classification.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monkey_classification.R
import com.example.monkey_classification.presentation.screenItem.ScreenItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navController: NavController = NavController(LocalContext.current),
    auth: FirebaseAuth
) {
    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.popBackStack()
        val currentUser: FirebaseUser? = auth.currentUser

        if(currentUser == null) {
            navController.navigate(ScreenItem.IntroScreenItem.route)
        } else {
            if (!currentUser.isEmailVerified) {
                navController.navigate(ScreenItem.IntroScreenItem.route)
            } else {
                navController.navigate(ScreenItem.HomeScreenItem.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),"Monkey World",
            modifier = Modifier.width(300.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}

