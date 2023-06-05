package com.example.mixed_drems_mobile.pages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixed_drems_mobile.MainActivity
import com.example.mixed_drems_mobile.api.auth.AuthServiceImpl
import com.example.mixed_drems_mobile.api.auth.CustomerRegisterRequest
import com.example.mixed_drems_mobile.api.auth.IAuthService
import com.example.mixed_drems_mobile.navigation.Routes
import com.example.mixed_drems_mobile.ui.theme.MixeddremsmobileTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavHostController, activity: ComponentActivity) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    register(email, password, confirmPassword, firstName, lastName, coroutineScope, activity, navController)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Signup")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Already a user? Login",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Login.route)
                }
            )
        }
    }
}

private fun register(
    email: String,
    password: String,
    confirmPassword: String,
    firstName: String,
    lastName: String,
    scope: CoroutineScope,
    context: Context,
    navController: NavHostController
) {
    if (email.isNotEmpty() && password.isNotEmpty() &&
        confirmPassword.isNotEmpty() && firstName.isNotEmpty() &&
        lastName.isNotEmpty()
    ) {
        if (password == confirmPassword) {
            scope.launch {
                val authService: IAuthService = AuthServiceImpl()
                val data = CustomerRegisterRequest(firstName, lastName, email, password)
                val result = authService.register(data)
                if (result.isSuccess) {
                    println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOKKKKKKKKKKKKKKKKKKKKKKKKKK")
                } else {
                    Toast
                        .makeText(context, result.error?.title, Toast.LENGTH_SHORT)
                        .show()
                }
                print(result)
            }
        } else {
            Toast.makeText(context, "Password does not matched", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    MixeddremsmobileTheme {
        Signup(rememberNavController(), MainActivity())
    }
}

