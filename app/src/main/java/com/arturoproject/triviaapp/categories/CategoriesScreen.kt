package com.arturoproject.triviaapp.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.arturoproject.triviaapp.TriviaTopBar

@Composable
fun CategoriesScreen(navController: NavController, userId: String) {



    //TO DO
    //IMPLEMENTING THE DATABASE TO SELECT CATEGORIES

    val categories = listOf(
        Category.MIXED,
        Category.GENERAL_HISTORY,
        Category.CLASSICAL_MUSIC,
        Category.MATH
    )

    var showDialog by remember { mutableStateOf(false) }
    var comingSoonCategory by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TriviaTopBar(
                navController = navController,
                title = "Categories",
                onSettingsClick = { navController.navigate("settings") } // Corrected placement
            )
        }
    ){ paddingValues ->
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(18.dp)
    ) {
        Column {
            Text(text = "Getting Ready!!", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            categories.forEach { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            //IF STATEMENT THE CATEFORIES ARE NOT DONE YET
                            if (category == Category.MIXED) {
                                showDialog = true
                                comingSoonCategory = category.displayName
                            } else {
                                navController.navigate("difficulty/${category.id}/$userId")
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text(text = category.displayName, fontSize = 18.sp)
                    }
                }
            }
        }

        //As we keep working if the feature
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Coming Soon!") },
                text = {
                    Column {


                        Text("The feature is coming soon!")

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { showDialog = false }) {
                            Text("OK")
                        }
                    }
                },
                confirmButton = {},
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                )
            )
        }
    }
}}
