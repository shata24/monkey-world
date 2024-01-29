package com.example.monkey_classification.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monkey_classification.presentation.util.monkeyList
import java.lang.Integer.parseInt

@Composable
fun MonkeyDetailsScreen(navController: NavController, id: String) {
    var monkey = monkeyList[parseInt(id)-1]
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                title = "Home"
            )
        },
        modifier = Modifier.background(Color(0xFFB0D5E9)),
        topBar = {
            AppBar(navController = navController, title = "Home")
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
            ) {
                // Monkey Image
                item {
                    Image(
                        painter = painterResource(id = monkey.image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(shape = MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }

                // Monkey Name
                item {
                    Text(
                        text = monkey.name,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Description
                item {
                    Text(
                        text = monkey.description,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}