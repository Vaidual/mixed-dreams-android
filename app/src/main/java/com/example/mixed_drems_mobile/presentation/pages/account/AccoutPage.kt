package com.example.mixed_drems_mobile.presentation.pages.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mixed_drems_mobile.navigation.Routes
import com.example.mixed_drems_mobile.utils.SharedPreferencesHelper

@Composable
fun AccountPage(
    navController: NavController
) {

    fun logout() {
        SharedPreferencesHelper.removeTokens()
        navController.navigate(Routes.Login.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp),
    ) {
        GetPreferenceList(logout = { logout() })
    }
}

@Composable
fun GetPreferenceList(
    logout: () -> Unit
) {
    Column() {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { logout() },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Log out",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp),
    ) {
        GetPreferenceList(logout = {})
    }
}