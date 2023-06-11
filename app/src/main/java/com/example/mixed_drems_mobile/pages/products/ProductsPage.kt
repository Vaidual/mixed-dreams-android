package com.example.mixed_drems_mobile.pages.products

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.mixed_drems_mobile.api.products.Product
import com.example.mixed_drems_mobile.api.products.ProductsServiceImpl
import com.example.mixed_drems_mobile.api.products.getProducts.ProductFilterParams
import com.example.mixed_drems_mobile.constants.ProductSort
import com.example.mixed_drems_mobile.ui.theme.MixeddremsmobileTheme
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ProductsPage(navController: NavHostController) {

    val productsService = ProductsServiceImpl(LocalContext.current)
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

    data class SortOption(val label: String, val key: String)

    val sortOptions = listOf(
        SortOption("Price: Low to High", ProductSort.Price),
        SortOption("Price: High to Low", ProductSort.PriceDesc),
        SortOption("Most New", ProductSort.New)
    )

    val defaultSort = sortOptions[2]
    val defaultFilterParams = ProductFilterParams(
        null,
        null,
        defaultSort.key
    )

    val vm = ProductsViewModel(productsService, defaultFilterParams)
    val products: LazyPagingItems<Product> = vm.productsFlow.collectAsLazyPagingItems()

    var currentSort by remember { mutableStateOf(defaultSort.label) }

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

    var openSearchBar by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

    fun updateSearchKey(key: String) {
        vm.updateFilter(vm.getFilter().copy(key = key))
    }

    products.loadState.source

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (!openSearchBar)
                            openSearchBar = true
                    },
                color = MaterialTheme.colorScheme.background,
            ) {
                TextField(
                    enabled = openSearchBar,
                    value = query,
                    onValueChange = {
                        query = it
                    },
                    placeholder = {
                        Text(
                            text = "Serch for product name",
                        )
                    },
//                    textStyle = TextStyle(
//                        fontSize = 16.sp
//                    ),
                    singleLine = true,
                    trailingIcon = {
                        if (openSearchBar)
                            IconButton(onClick = {
                                updateSearchKey("")
                                query = ""
                                openSearchBar = false
                            }) {
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
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            updateSearchKey(query)
                            openSearchBar = false
                        }
                    ),
                    colors = TextFieldDefaults.colors(

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                        .padding(0.dp)
                )
            }
//                TopAppBar(
//                    title = {
//                        Text(
//                            modifier = Modifier.fillMaxWidth(),
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.SemiBold,
//                            text = "Products",
//                        )
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
//                    actions = {
//                        IconButton(onClick = {
//                            openSearchBar = true
//                        }) {
//                            Icon(
//                                imageVector = Icons.Default.Search,
//                                contentDescription = "Search Icon"
//                            )
//                        }
//                    }
//                )
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
                        .padding(10.dp)
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
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = CenterHorizontally,
                ) {
                    //                stickyHeader {
                    //                    Box(
                    //                        modifier = Modifier
                    //                            .background(MaterialTheme.colorScheme.background)
                    //                            .fillMaxWidth()
                    //                    ) {
                    //                        Row(
                    //                            horizontalArrangement = Arrangement.Start
                    //                        ) {
                    //                            TextButton(onClick = { openSortSheet = !openSortSheet }) {
                    //                                Icon(
                    //                                    imageVector = Icons.Rounded.Sort,
                    //                                    contentDescription = "Sort Icon",
                    //                                )
                    //                                Text(text = currentSort)
                    //                            }
                    //                        }
                    //                    }
                    //                    Spacer(modifier = Modifier.height(4.dp))
                    //                }
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

//@Preview(showBackground = true)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Test() {
    MixeddremsmobileTheme {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    text = "Home",
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            }
        )
    }
}
