package com.example.monkey_classification.presentation.screens

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monkey_classification.ui.theme.secondaryColor

@Composable
fun AppBar(navController: NavController, title: String) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        backgroundColor = secondaryColor,
        contentColor = Color.White,
        elevation = 10.dp
    )
}