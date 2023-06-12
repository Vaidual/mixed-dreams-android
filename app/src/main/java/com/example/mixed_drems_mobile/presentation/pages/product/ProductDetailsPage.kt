package com.example.mixed_drems_mobile.presentation.pages.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun ProductDetailsPage(
    navigateBack: () -> Boolean,
    vm: ProductDetailsViewModel = hiltViewModel()
) {
    val state = vm.state.value

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(

        ) {
            AsyncImage(
                model = state.product?.primaryImage,
                contentDescription = "Product image",
                Modifier.fillMaxWidth().heightIn(0.dp, 40.dp),
            )
        }
    }
}