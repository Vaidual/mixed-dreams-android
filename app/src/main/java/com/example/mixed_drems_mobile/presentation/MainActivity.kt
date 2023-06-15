package com.example.mixed_drems_mobile.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mixed_drems_mobile.presentation.pages.MainPage
import com.example.mixed_drems_mobile.presentation.theme.MixeddremsmobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MixeddremsmobileTheme() {
                MainPage(this)
            }
        }
    }
}

