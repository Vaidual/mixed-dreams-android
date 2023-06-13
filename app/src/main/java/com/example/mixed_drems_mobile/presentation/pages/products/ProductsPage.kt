package com.example.mixed_drems_mobile.presentation.pages.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.mixed_drems_mobile.api.products.Product
import com.example.mixed_drems_mobile.api.products.toCartItem
import com.example.mixed_drems_mobile.constants.ProductSort
import com.example.mixed_drems_mobile.navigation.Routes
import com.example.mixed_drems_mobile.presentation.theme.MixeddremsmobileTheme
import com.example.mixed_drems_mobile.utils.MainApplication
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class
)
@Composable
fun ProductsPage(navController: NavHostController, vm: ProductsViewModel = hiltViewModel()) {
    data class SortOption(val label: String, val key: String)

    val sortOptions = listOf(
        SortOption("Price: Low to High", ProductSort.Price),
        SortOption("Price: High to Low", ProductSort.PriceDesc),
        SortOption("Most New", ProductSort.New)
    )

    val defaultSort = sortOptions.first { o -> o.key == vm.getFilter().sort }

    val products: LazyPagingItems<Product> = vm.productsFlow.collectAsLazyPagingItems()

    var currentSort by remember { mutableStateOf(defaultSort.label) }

    var openSortSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    fun updateSortOrder(option: SortOption) {
        vm.updateFilter(vm.getFilter().copy(sort = option.key))
        currentSort = option.label
        openSortSheet = false
    }

    var openSearchBar by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(openSearchBar) {
        if (openSearchBar) {
            focusRequester.requestFocus()
        }
    }

    fun updateSearchKey(key: String) {
        vm.updateFilter(vm.getFilter().copy(key = key))
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .padding(6.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            if (!openSearchBar) {
                                openSearchBar = true
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        if (query == "") {
                            Text(
                                modifier = Modifier
                                    .padding(10.dp),
                                text = "Search for product here",
                                color = Color.Gray
                            )
                        }
                        BasicTextField(
                            textStyle = MaterialTheme.typography.bodyMedium,
                            enabled = openSearchBar,
                            value = query,
                            onValueChange = {
                                query = it
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    updateSearchKey(query)
                                    openSearchBar = false
                                }
                            ),
                            modifier = Modifier
                                .padding(10.dp)
                                .focusRequester(focusRequester),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    if (openSearchBar)
                        IconButton(onClick = {
                            query = ""
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear search icon",
                            )
                        }
                    else
                        Icon(
                            modifier = Modifier.padding(12.dp),
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                }
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        TextButton(onClick = { openSortSheet = !openSortSheet }) {
                            Icon(
                                imageVector = Icons.Rounded.Sort,
                                contentDescription = "Sort Icon",
                            )
                            Text(text = currentSort)
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = CenterHorizontally,
                ) {
                    if (products.loadState.refresh == LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Center
                            ) {
                                CircularProgressIndicator(
                                )
                            }
                        }
                    } else {
                        items(
                            count = products.itemCount,
                            key = products.itemKey(),
                            contentType = products.itemContentType(
                            )
                        ) { index ->
                            val item = products[index]
                            if (item != null) {
                                ProductCard(item, navController)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    if (products.loadState.append == LoadState.Loading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(CenterHorizontally)
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
                            ListItem(
                                modifier = Modifier.clickable { openSortSheet = false },
                                headlineContent = {
                                    Text(
                                        text = "Sort by",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                },
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            sortOptions.forEach { option ->
                                if (option.label == currentSort)
                                    ListItem(
                                        modifier = Modifier.clickable { updateSortOrder(option) },
                                        headlineContent = {
                                            Text(
                                                text = option.label,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        },
                                        trailingContent = {
                                            Icon(
                                                Icons.Rounded.Done,
                                                tint = MaterialTheme.colorScheme.primary,
                                                contentDescription = "Selected sort option icon"
                                            )
                                        }
                                    )
                                else
                                    ListItem(
                                        modifier = Modifier.clickable { updateSortOrder(option) },
                                        headlineContent = {
                                            Text(
                                                text = option.label,
                                            )
                                        },
                                    )
                            }
                            ListItem(
                                modifier = Modifier
                                    .clickable {
                                        scope
                                            .launch { bottomSheetState.hide() }
                                            .invokeOnCompletion {
                                                if (!bottomSheetState.isVisible) {
                                                    openSortSheet = false
                                                }
                                            }
                                    }
                                    .padding(4.dp),
                                headlineContent = {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        text = "CANCEL",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.hsl(214F, 0F, 0.3F, 1F)
                                    )
                                },
                            )
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun ProductCard(product: Product, navController: NavHostController) {
    Card(
        modifier = Modifier
            .widthIn(0.dp, 300.dp)
            .height(210.dp)
            .clickable{
                navController.navigate(Routes.Product.createRoute(product.id))
            },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
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
                    onClick = { MainApplication.instance.shoppingCart.addItem(product.toCartItem(1)) },
                ) {
                    Text(text = "Buy")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun LazyColumnPreview() {
    MixeddremsmobileTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .wrapContentHeight(),
            horizontalAlignment = CenterHorizontally,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Test() {
    MixeddremsmobileTheme {
        Row(
            Modifier
                .padding(6.dp)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
            ) {
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = "",
                    color = Color.Gray
                )
                BasicTextField(
                    textStyle = MaterialTheme.typography.bodyMedium,
                    enabled = true,
                    value = "eefef",
                    onValueChange = {

                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {

                        }
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            if (true)
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear search icon",
                    )
                }
            else
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
        }
    }
}
