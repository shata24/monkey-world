package com.example.monkey_classification.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monkey_classification.R
import com.example.monkey_classification.presentation.screenItem.ScreenItem
import com.example.monkey_classification.ui.theme.primaryColor

@Preview
@Composable
fun IntroScreen(navController: NavController = NavController(LocalContext.current)) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Monkey World",
            color = Color.Black,
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier.padding(horizontal = 20.dp) ,
            painter = painterResource(R.drawable.monkeydemo), contentDescription = "registration"
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Let's get started",
            color = Color.Gray,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))

        val modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)

        AuthButton(
            modifier = modifier,
            text = "SIGN IN",
            onClick = {
                navController.navigate(ScreenItem.LoginScreenItem.route)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AuthButton(
            modifier = modifier,
            text = "SIGN UP",
            onClick = {
                navController.navigate(ScreenItem.RegistrationScreenItem.route)
            }
        )
    }
}

@Composable
fun AuthButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(primaryColor)
            .clickable {
                onClick()
            }
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.h6
        )
    }
}
