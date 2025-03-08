package com.arturoproject.triviaapp

import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.arturoproject.triviaapp.data.QuestionEntity
import com.arturoproject.triviaapp.data.TriviaDatabase

@Composable
fun TriviaTopBar(
    navController: NavController,
    title: String,
    currentQuestion: QuestionEntity? = null,  // Ahora es opcional
    database: TriviaDatabase? = null,  // Ahora es opcional
    onSettingsClick: () -> Unit,
    userId: Int = 0
) {
    var showDialog by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf("Loading hint...") }

    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = "Settings")
            }
            IconButton(onClick = { navController.navigate("profile/$userId") }) {
                Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = "Account")
            }


            // The bottom only appears if there is any questiomn
            if (currentQuestion != null && database != null) {
                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "Hint")
                }
            }
        }
    )

    if (showDialog && currentQuestion != null && database != null) {
        LaunchedEffect(Unit) {
            hint = database.questionDao().getHint(currentQuestion.id)
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Need Help?") },
            text = { Text(hint) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}