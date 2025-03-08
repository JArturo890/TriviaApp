package com.arturoproject.triviaapp.difficulty

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arturoproject.triviaapp.TriviaTopBar
import com.arturoproject.triviaapp.data.TriviaDatabase


@Composable
fun DifficultyScreen(navController: NavController, category: Int, database: TriviaDatabase, userId: Int) {
    val difficulties = mapOf("Easy" to 1, "Medium" to 2, "Hard" to 3)

    Scaffold(
        topBar = {
            TriviaTopBar(
                navController = navController,
                title = "Difficulty",
                onSettingsClick = { navController.navigate("settings") } // Corrected placement
            )
        }
    ) { paddingValues ->

        Box(Modifier.fillMaxSize().padding(16.dp)) {
            Column {
                Text(text = "Category: $category", fontSize = 20.sp)
                Text(text = "Select Difficulty", fontSize = 24.sp)

                Spacer(modifier = Modifier.height(16.dp))

                difficulties.forEach { (difficultyText, difficultyValue) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValues)
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("trivia/$category/$difficultyValue/$userId")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(text = difficultyText, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}


