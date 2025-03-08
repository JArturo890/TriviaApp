package com.arturoproject.triviaapp

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arturoproject.triviaapp.data.QuestionEntity
import com.arturoproject.triviaapp.data.TriviaDatabase

@Composable
fun TriviaScreen(navController: NavController, category: Int, difficulty: Int, userId: Int = 0, database: TriviaDatabase) {
    val questionDao = remember { database.questionDao() }

    var questions by remember { mutableStateOf<List<QuestionEntity>>(emptyList()) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var showFinalScore by remember { mutableStateOf(false) }
    var isAnswered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        questions = questionDao.getQuestionsByCategoryAndDifficulty(category, difficulty)
        currentQuestionIndex = 0
    }

    Scaffold(
        topBar = {
            TriviaTopBar(
                navController = navController,
                title = "Category: $category (Difficulty: $difficulty)",
                currentQuestion = questions.getOrNull(currentQuestionIndex),
                database = database,
                userId = userId,
                onSettingsClick = { navController.navigate("settings") }
            )
        }
    ) { paddingValues ->
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading questions...", style = MaterialTheme.typography.headlineMedium)
            }
        } else if (showFinalScore) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Game Over!", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Your final score: $score", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {

                    val userId = "someUserId"  //toavoidcrash
                    navController.navigate("categories/$userId") {
                        popUpTo("categories") { inclusive = true }
                    }
                }) {
                    Text(text = "Go Back to Categories")
                }


            }
        } else if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = question.question, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))


                listOf(question.option1, question.option2, question.option3).forEach { option ->
                    Button(
                        onClick = {
                            if (!isAnswered) {
                                isCorrect = option == question.answer
                                if (isCorrect) {
                                    score += when (difficulty) {
                                        1 -> 10  // Easy
                                        2 -> 20  // Medium
                                        3 -> 50  // Hard
                                        else -> 0
                                    }
                                }
                                isAnswered = true
                                showFeedback = true
                            }
                        },
                        enabled = !isAnswered,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                isAnswered && option == question.answer -> Color.Green
                                isAnswered && option != question.answer -> Color.Red
                                else -> MaterialTheme.colorScheme.primary
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    ) {
                        Text(text = option)
                    }
                }

                if (showFeedback) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = if (isCorrect) "That is Correct!" else "Incorrect :(",
                        color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            showFeedback = false
                            isAnswered = false
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                showFinalScore = true
                            }
                        }
                    ) {
                        Text(text = "Next Question")
                    }
                }
            }
        }
    }
}
