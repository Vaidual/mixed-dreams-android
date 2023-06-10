package com.example.mixed_drems_mobile.pages.products

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.mixed_drems_mobile.api.products.Product
import com.example.mixed_drems_mobile.api.products.ProductsServiceImpl
import com.example.mixed_drems_mobile.api.products.getProducts.ProductFilterParams
import com.example.mixed_drems_mobile.constants.ProductSort
import com.example.mixed_drems_mobile.paging.ProductsPagingSource
import com.example.mixed_drems_mobile.ui.theme.MixeddremsmobileTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.paging.compose.LazyPagingItems

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ProductsPage(navController: NavHostController, activity: ComponentActivity) {

    val productsService = ProductsServiceImpl(activity)
//    val pageSize = 10;
//
//    var pagingParams by remember {
//        mutableStateOf(
//            ProductFilterParams(
//                null,
//                null,
//                null
//            )
//        )
//    }
//
//    val pagingConfig = PagingConfig(
//        pageSize = pageSize, // Number of items loaded per page
//        enablePlaceholders = false // Whether to show placeholders for unloaded items
//    )
//
//    val pagingSource = ProductsPagingSource(productsService, pagingParams, pageSize)
//
//    val pager = Pager(config = pagingConfig) {
//        pagingSource
//    }
//
//    val products = pager.flow.collectAsLazyPagingItems()
//
//    LaunchedEffect(products) {
//        // Coroutine scope for the effect
//        try {
//            // Start collecting the flow
//            products.refresh()
//        } catch (e: Exception) {
//            // Handle any exceptions
//            // Log or display an error message
//        }
//    }

    val vm: ProductsViewModel = ProductsViewModel(productsService)
    val products: LazyPagingItems<Product> = vm.productsFlow.collectAsLazyPagingItems()

    data class SortOption(val label: String, val key: String)

    val sortOptions = listOf(
        SortOption("Price Asc", ProductSort.Price),
        SortOption("Price Desc", ProductSort.PriceDesc),
        SortOption("Most New", ProductSort.New)
    )

    var currentSort by remember { mutableStateOf(sortOptions[0].label) }

    var openSortSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    fun updateSortOrder(option: SortOption) {
        vm.updateFilter(vm.getFilter().copy(sort = option.key))
        currentSort = option.label
        openSortSheet = false
//        pagingParams = ProductFilterParams(sortField, null, null)
//        products.refresh()
    }

    Scaffold(

    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Button(onClick = { openSortSheet = !openSortSheet }) {
                                Text(text = currentSort)
                            }
                        }
                    }
                }
                if (products.loadState.refresh == LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                            )
                        }
                    }
                }
                items(
                    count = products.itemCount,
                    key = products.itemKey(),
                    contentType = products.itemContentType(
                    )
                ) { index ->
                    val item = products[index]
                    if (item != null) {
                        ProductCard(item)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (products.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
            if (openSortSheet) {
                ModalBottomSheet(
                    onDismissRequest = { openSortSheet = false },
                    sheetState = bottomSheetState,
                ) {
                    Column {
                        sortOptions.forEach { option ->
                            ListItem(
                                modifier = Modifier.clickable { updateSortOrder(option) },
                                headlineContent = { Text(text = option.label) },
                                //                        leadingContent = {
                                //                            Icon(
                                //                                Icons.Default.Favorite,
                                //                                contentDescription = "Localized description"
                                //                            )
                                //                        }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .widthIn(0.dp, 300.dp)
            .height(210.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(

        ) {
            if (product.primaryImage != null) AsyncImage(
                model = product.primaryImage,
                contentDescription = "Image of product",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
            else Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = product.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "${product.price}$",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                OutlinedButton(
                    modifier = Modifier.defaultMinSize(minHeight = 32.dp),
                    onClick = { onAddToCartClicked(product.id) },
                ) {
                    Text(text = "Buy")
                }
            }
        }
    }
}

fun onAddToCartClicked(id: String) {

}

//@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MixeddremsmobileTheme {
        ProductCard(
            Product(
                "w",
                "Ricotto",
                "",
                120.0f,
                null
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LazyColumnPreview() {
    MixeddremsmobileTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red),

                    ) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}