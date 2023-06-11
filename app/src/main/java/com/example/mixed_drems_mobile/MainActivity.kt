package com.example.mixed_drems_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mixed_drems_mobile.pages.MainPage
import com.example.mixed_drems_mobile.ui.theme.MixeddremsmobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MixeddremsmobileTheme {
                MainPage(this)
            }
        }
    }
}

