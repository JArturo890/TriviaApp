package com.arturoproject.triviaapp.loginui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var showHelpDialog by remember { mutableStateOf(false) }  // Para controlar cuándo mostrar el diálogo

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TriviaApp") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(imageVector = Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Login(
                    modifier = Modifier,
                    viewModel = viewModel,
                    navController = navController
                )

                // Botón de ayuda
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showHelpDialog = true }) {
                    Text(text = "Help")
                }

                // Mostrar el diálogo de ayuda
                if (showHelpDialog) {
                    HelpDialog(onDismiss = { showHelpDialog = false })
                }
            }
        }
    }
}

@Composable
fun HelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Help") },
        text = {
            Column {
                Text("App Purpose:")
                Spacer(modifier = Modifier.height(4.dp))
                Text("This is a trivia game where you can test your knowledge!")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Preferences:")
                Spacer(modifier = Modifier.height(4.dp))
                Text("You can toggle the darkmode in the settings")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Hint:")
                Spacer(modifier = Modifier.height(4.dp))
                Text("You can hit the search button to see the hint for the question.")

            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController) {
    val email by viewModel.email.observeAsState(initial = "")
    val loginEnable by viewModel.loginEnable.observeAsState(initial = false)
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(modifier = modifier) {
            EmailField(email) { viewModel.onLoginChanged(it) }
            Spacer(modifier = Modifier.padding(8.dp))
            LoginButton(loginEnable) {
                coroutineScope.launch {
                    isLoading = true
                    viewModel.onLoginSelected()
                    isLoading = false
                    val userId = email.hashCode() //Get the user Id.
                    navController.navigate("categories/$userId")

                }
            }
        }
    }
}

@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = loginEnable
    ) {
        Text(text = "Log In")
    }
}

@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Enter your name") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}