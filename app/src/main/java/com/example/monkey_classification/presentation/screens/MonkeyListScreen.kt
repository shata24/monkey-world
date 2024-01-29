package com.example.monkey_classification.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medi_sheba.presentation.util.gridItems
import com.example.monkey_classification.presentation.screenItem.ScreenItem
import com.example.monkey_classification.presentation.util.monkeyList

@Composable
fun MonkeyListScreen(navController: NavController) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Monkey World",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                gridItems(
                    data = monkeyList,
                    columnCount = 2,
                    modifier = Modifier
                ) { monkey ->
                    MonkeyCard(
                        modifier = Modifier.clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set("id", monkey.id.toString())
                            navController.navigate(ScreenItem.MonkeyDetailsScreenItem.route)
                        },
                        name = monkey.name,
                        contentName = monkey.name,
                        painter = painterResource(monkey.image)
                    )
                }
            }
        }
    }
}

@Composable
fun MonkeyCard(
    modifier: Modifier = Modifier,
    name: String,
    contentName: String,
    painter: Painter,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 7.dp, vertical = 5.dp)
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(all = 5.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = contentName,
            modifier = Modifier
                .width(90.dp)
                .padding(vertical = 10.dp)
        )
        Text(
            text = name,
            modifier = Modifier.padding(bottom = 5.dp),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
        )
    }
}