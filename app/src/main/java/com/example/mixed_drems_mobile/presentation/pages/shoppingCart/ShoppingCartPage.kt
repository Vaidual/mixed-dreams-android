package com.example.mixed_drems_mobile.presentation.pages.shoppingCart

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mixed_drems_mobile.R
import com.example.mixed_drems_mobile.models.CartItem
import com.example.mixed_drems_mobile.presentation.elements.BaseDivider
import com.example.mixed_drems_mobile.presentation.elements.QuantitySelector
import com.example.mixed_drems_mobile.utils.MainApplication
import com.example.mixed_drems_mobile.utils.formatPrice
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetContract
import com.stripe.android.paymentsheet.addresselement.AddressDetails
import kotlinx.coroutines.delay

@Composable
fun ShoppingCartPage(
    goToProduct: (id: String) -> Unit,
    viewModel: StripeIntentViewModel = hiltViewModel()
) {
    val stripeLauncher = rememberLauncherForActivityResult(contract = PaymentSheetContract()) {
        viewModel.handlePaymentResult(it)
    }

    val clientSecret by viewModel.state.collectAsState()
    clientSecret.data?.clientSecret?.let {
        stripeLauncher.launch(
            PaymentSheetContract.Args.createPaymentIntentArgs(
                it,
                config = PaymentSheet.Configuration(
                    merchantDisplayName = "Mixed Dreams",
                    shippingDetails = AddressDetails(
                        address = PaymentSheet.Address(
                            country = "UA"
                        )
                    )
                )

            )
        )
        viewModel.onPaymentLaunched()
    }


    data class RemoveItemState(
        var isOpen: Boolean = false,
        val itemId: String = "",
        val itemName: String = ""
    )

    val removeItemDialogState = remember { mutableStateOf(RemoveItemState()) }

    val state = viewModel.state.collectAsState()

    if (state.value.successOrder) {
        LaunchedEffect(Unit) {
            delay(2000) // Delay for 2 seconds
            viewModel.onSuccessEnd()
        }
    }
    val transition = updateTransition(targetState = state.value.successOrder, label = "SuccessWindowTransition")

    val successIconColor by transition.animateColor(label = "IconColorTransition") { x ->
        if (x) Color.White else Color.Transparent
    }
    val backgroundAlpha by transition.animateFloat(label = "BackgroundAlphaTransition") { x ->
        if (x) 0.8f else 0f
    }
    val contentScale by transition.animateFloat(label = "ContentScaleTransition") { x ->
        if (x) 1f else 0.7f}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).zIndex(10000000f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = successIconColor,
            modifier = Modifier
                .size(120.dp)
                .scale(contentScale)
                .padding(16.dp)
                .background(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = backgroundAlpha)
                )
        )
        Text(
            text = "Order Placed Successfully!",
            color = MaterialTheme.colorScheme.primary.copy(alpha = backgroundAlpha),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

    AnimatedVisibility(
        visible = state.value.successOrder,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha)).zIndex(1000000f),
        )
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp),
    ) {

        if (MainApplication.instance.shoppingCart.cartItems.isNotEmpty()) {
            Column {
                Spacer(modifier = Modifier.statusBarsPadding())
                MainApplication.instance.shoppingCart.cartItems.map { cartItem ->
                    CartItemWidget(
                        item = cartItem,
                        decreaseItemCount = { id ->
                            MainApplication.instance.shoppingCart.decrementItem(
                                id
                            )
                        },
                        increaseItemCount = { id ->
                            MainApplication.instance.shoppingCart.incrementItem(
                                id
                            )
                        },
                        goToProduct = { id -> goToProduct(id) },
                        showRemoveItemDialog = { id, name ->
                            removeItemDialogState.value = RemoveItemState(true, id, name)
                        },
                    )
                }
                BaseDivider(
                    Modifier.padding(horizontal = 12.dp)
                )
                SummaryItem(subtotal = MainApplication.instance.shoppingCart.cartItems.sumOf { it.price * it.count })
            }
            CheckoutBar(modifier = Modifier.align(Alignment.BottomCenter), onCheckout = { viewModel.createPaymentIntent(amount = (MainApplication.instance.shoppingCart.cartItems.sumOf { it.price * it.count } * 100).toLong())})
        } else {
            Image(
                painter = painterResource(id = R.drawable.empty_cart),
                contentDescription = "Empty cart image",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
    if (removeItemDialogState.value.isOpen) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                removeItemDialogState.value = RemoveItemState(isOpen = false)
            },
            title = {
                Text(text = buildAnnotatedString {
                    append("Remove ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(removeItemDialogState.value.itemName)
                    }
                    append(" from cart?")
                })
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        MainApplication.instance.shoppingCart.removeItem(removeItemDialogState.value.itemId)
                        removeItemDialogState.value = RemoveItemState(isOpen = false)
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        removeItemDialogState.value = RemoveItemState(isOpen = false)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CartItemWidget(
    item: CartItem,
    showRemoveItemDialog: (id: String, name: String) -> Unit,
    increaseItemCount: (String) -> Unit,
    decreaseItemCount: (String) -> Unit,
    goToProduct: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clickable { goToProduct(item.id) }
                .padding(end = 12.dp),
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = "${item.name} image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )
        }
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    modifier = Modifier.clickable { goToProduct(item.id) },
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                IconButton(
                    onClick = { showRemoveItemDialog(item.id, item.name) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove item ${item.name}"
                    )
                }
            }
            Spacer(
                Modifier
                    .height(18.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatPrice(item.price * item.count),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                QuantitySelector(
                    count = item.count,
                    decreaseItemCount = {
                        if (item.count > 1)
                            decreaseItemCount(item.id)
                        else if (item.count == 1) {
                            showRemoveItemDialog(item.id, item.name)
                        }
                    },
                    increaseItemCount = { increaseItemCount(item.id) },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CheckoutBar(
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        BaseDivider()
        Row {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { onCheckout() },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    text = "Checkout",
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
            }
        }
    }
}




@Composable
fun SummaryItem(
    subtotal: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = "Summary",
            style = androidx.compose.material.MaterialTheme.typography.h6,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Subtotal",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal),
                style = androidx.compose.material.MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        BaseDivider()
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            Text(
                text = "Total",
                style = androidx.compose.material.MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .wrapContentWidth(Alignment.End)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        BaseDivider()
    }
}

@Composable
@Preview
fun ShoppingCartPreview() {
    val cartItems =
        listOf(
            CartItem("", "Закуска", "", 12.2, 5),
            CartItem("", "Закуска", "", 12.2, 5)
        )
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp),
    ) {
        if (true) {
            Column {
                cartItems.map { cartItem ->
                    CartItemWidget(
                        item = cartItem,
                        decreaseItemCount = { id ->
                            MainApplication.instance.shoppingCart.decrementItem(
                                id
                            )
                        },
                        increaseItemCount = { id ->
                            MainApplication.instance.shoppingCart.incrementItem(
                                id
                            )
                        },
                        goToProduct = { },
                        showRemoveItemDialog = { _, _ -> },
                    )
                }
                SummaryItem(subtotal = cartItems.sumOf { it.price * it.count })
            }
            CheckoutBar(modifier = Modifier.align(Alignment.BottomCenter), onCheckout = {})
        } else {
            Image(
                painter = painterResource(id = R.drawable.empty_cart),
                contentDescription = "Empty cart image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            )
        }
    }
}