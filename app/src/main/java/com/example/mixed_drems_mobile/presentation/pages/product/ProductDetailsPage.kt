package com.example.mixed_drems_mobile.presentation.pages.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.mixed_drems_mobile.R
import com.example.mixed_drems_mobile.api.products.getProductDetails.GetProductDetailsResponse
import com.example.mixed_drems_mobile.api.products.getProductDetails.IngredientDto
import com.example.mixed_drems_mobile.api.products.getProductDetails.ProductCompanyDto
import com.example.mixed_drems_mobile.api.products.getProductDetails.toCartItem
import com.example.mixed_drems_mobile.enums.MeasureUnit
import com.example.mixed_drems_mobile.enums.getShortWithNumber
import com.example.mixed_drems_mobile.presentation.elements.BaseDivider
import com.example.mixed_drems_mobile.presentation.elements.ExpandableText
import com.example.mixed_drems_mobile.presentation.elements.PulseLoading
import com.example.mixed_drems_mobile.presentation.elements.QuantitySelector
import com.example.mixed_drems_mobile.utils.MainApplication

private val HzPadding = Modifier.padding(horizontal = 24.dp)
private val BottomBarHeight = 56.dp

@Composable
fun ProductDetailsPage(
    navigateBack: () -> Boolean,
    vm: ProductDetailsViewModel = hiltViewModel()
) {
    val state = vm.state.value

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.product != null) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.product.primaryImage != null) {
                    Box(
                        Modifier.height(300.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = state.product.primaryImage,
                            contentDescription = "Product image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                PulseLoading()
                            }
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            Pair(0.7f, Transparent),
                                            Pair(1f, MaterialTheme.colorScheme.background)
                                        )
                                    )
                                )
                        )
                    }
                } else {
                    Box(modifier = Modifier.height(50.dp))
                }
                Column(
                    Modifier.padding(horizontal = 12.dp)
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp)),
                        ) {
                            Text(
                                state.product.category,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                                    .padding(vertical = 4.dp, horizontal = 18.dp),

                                )
                        }
                        Box(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .clip(RoundedCornerShape(20.dp)),
                        ) {
                            Text(
                                text = "${state.product.preparationTime} min",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                                    .padding(vertical = 4.dp, horizontal = 18.dp),

                                )
                        }
                    }
                    Text(
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                        text = state.product.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    ExpandableText(
                        text =
                        if (state.product.description != "")
                            state.product.description
                        else
                            "You should try it!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        collapsedMaxLine = 6,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    if (state.product.ingredients.isNotEmpty()) {
                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        state.product.ingredients.forEach { ingredient ->
                            Row(
                                Modifier.padding(4.dp),
                                verticalAlignment = CenterVertically
                            ) {
                                Text(text = "• ")
                                Text(
                                    text = ingredient.name,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                if (ingredient.hasAmount) {
                                    Text(
                                        text = ingredient.unit!!.getShortWithNumber(ingredient.amount!!),
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            CartBottomBar(
                product = state.product,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 32.dp, top = 16.dp)
                .height(56.dp)
        ) {
            IconButton(
                onClick = { navigateBack() },
                modifier = Modifier
                    .statusBarsPadding()
                    .size(36.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CircularButton(
    icon: @Composable () -> Unit,
    backgroundColor: Color = Color.LightGray,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
        ),
        elevation = elevation,
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)

    ) {
        icon()
    }
}


@Preview
@Composable
fun BoxPreview() {
    val product = GetProductDetailsResponse(
        "",
        "YAMMIII goody",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        1222.12,
        null,
        "Salad",
        ProductCompanyDto("", "Company1"),
        listOf(
            IngredientDto("Apple", true, 12f, MeasureUnit.Milliliter),
            IngredientDto("wefewfw", true, 23.4f, MeasureUnit.Pint),
            IngredientDto("UHuwddwdwdwdwd", false, 12f, MeasureUnit.Milliliter)
        ),
        12
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                Modifier.height(300.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.product),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.7f, Transparent),
                                    Pair(1f, MaterialTheme.colorScheme.background)
                                )
                            )
                        )
                )
            }
            Column(
                Modifier.padding(horizontal = 12.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp)),
                    ) {
                        Text(
                            product.category,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                                .padding(vertical = 4.dp, horizontal = 18.dp),

                            )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clip(RoundedCornerShape(20.dp)),
                    ) {
                        Text(
                            text = "${product.preparationTime} min",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                                .padding(vertical = 4.dp, horizontal = 18.dp),

                            )
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                ExpandableText(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    collapsedMaxLine = 6,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                product.ingredients.forEach { ingredient ->
                    Row(
                        Modifier.padding(4.dp),
                        verticalAlignment = CenterVertically
                    ) {
                        Text(text = "• ")
                        Text(
                            text = ingredient.name,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        if (ingredient.hasAmount) {
                            Text(
                                text = ingredient.unit!!.getShortWithNumber(ingredient.amount!!),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        }
        CartBottomBar(product = product, modifier = Modifier.align(Alignment.BottomCenter))
    }
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 32.dp, top = 16.dp)
            .height(56.dp)
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .statusBarsPadding()
                .size(36.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = Color.White,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun CartBottomBar(product: GetProductDetailsResponse, modifier: Modifier = Modifier) {
    val (count, updateCount) = remember { mutableStateOf(1) }
    Surface(modifier) {
        Column {
            BaseDivider()
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding()
                    .then(HzPadding)
                    .heightIn(min = BottomBarHeight)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                QuantitySelector(
                    count = count,
                    decreaseItemCount = { if (count > 1) updateCount(count - 1) },
                    increaseItemCount = { updateCount(count + 1) }
                )
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = {
                        MainApplication.instance.shoppingCart.addItem(product.toCartItem(count))
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "ADD TO CART",
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}


@Composable
fun BaseIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    // This should use a layer + srcIn but needs investigation
    Surface(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            )
            .clip(CircleShape),
        color = Transparent
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}