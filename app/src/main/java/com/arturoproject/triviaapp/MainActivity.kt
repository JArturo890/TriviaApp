package com.arturoproject.triviaapp


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arturoproject.triviaapp.loginui.LoginScreen
import com.arturoproject.triviaapp.categories.CategoriesScreen
import com.arturoproject.triviaapp.difficulty.DifficultyScreen
import com.arturoproject.triviaapp.loginui.LoginViewModel
import com.arturoproject.triviaapp.loginui.LoginViewModelFactory
import com.arturoproject.triviaapp.ui.theme.TriviaappTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.arturoproject.triviaapp.data.TriviaDatabase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Database initialization
        val triviaDatabase = TriviaDatabase.getDatabase(applicationContext)

        // ViewModel initialization
        val loginViewModel: LoginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(applicationContext)
        )[LoginViewModel::class.java]

        setContent {
            // Check if dark mode is enabled
            val darkTheme by loginViewModel.isDarkMode.collectAsStateWithLifecycle(initialValue = false)

            // Show dialog every time
            var showDialog by remember { mutableStateOf(true) }

            TriviaappTheme(darkTheme = darkTheme) {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Trivia App") },
                        text = { Text("TriviaApp v1.0(08.03.2025)")
                        },
                        confirmButton = {
                            Button(onClick = { showDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }

                MainNavHost(navController = rememberNavController(), loginViewModel, triviaDatabase)
            }
        }
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    triviaDatabase: TriviaDatabase
) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }
        composable("categories/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CategoriesScreen(navController, userId) // Pasa el userId como String
        }

        composable("difficulty/{category}/{userId}",
            arguments = listOf(
                navArgument("category") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getInt("category") ?: 1
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            DifficultyScreen(navController, category, triviaDatabase, userId) // Pass userId
        }
        composable("trivia/{category}/{difficulty}/{userId}",
            arguments = listOf(
                navArgument("category") { type = NavType.IntType },
                navArgument("difficulty") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getInt("category") ?: 1
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            TriviaScreen(navController, category, difficulty, userId, triviaDatabase)
        }
        composable("settings") {
            SettingsScreen(navController, loginViewModel)
        }
        composable(
            "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            ProfileScreen(navController, userId, triviaDatabase)
        }

    }
}