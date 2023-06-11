package com.example.mixed_drems_mobile.pages.auth.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixed_drems_mobile.api.auth.AuthServiceImpl
import com.example.mixed_drems_mobile.api.auth.IAuthService
import com.example.mixed_drems_mobile.api.auth.login.LoginRequest
import com.example.mixed_drems_mobile.navigation.Routes
import com.example.mixed_drems_mobile.ui.theme.MixeddremsmobileTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController, activity: Context) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = { login(email, password, coroutineScope, activity, navController) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Login")
        }
        TextButton(onClick = {
            navController.navigate(Routes.Signup.route)
        }) {
            Text(
                text = "Don't have an account? Create a new one",
                letterSpacing = 1.sp,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

private fun login(
    email: String,
    password: String,
    scope: CoroutineScope,
    context: Context,
    navController: NavHostController
) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        scope.launch {
            val authService: IAuthService = AuthServiceImpl(context)
            val data = LoginRequest(email, password, true)
            val result = authService.login(data)
            if (result.isSuccess) {
                navController.navigate(Routes.Products.route)
            } else {
                Toast
                    .makeText(context, result.error?.title, Toast.LENGTH_SHORT)
                    .show()
            }
            print(result)
        }
    } else {
        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MixeddremsmobileTheme {
        Login(rememberNavController(), LocalContext.current)
    }
}
