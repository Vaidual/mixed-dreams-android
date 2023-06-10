package com.example.mixed_drems_mobile.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mixed_drems_mobile.api.products.IProductsService
import com.example.mixed_drems_mobile.api.products.Product
import com.example.mixed_drems_mobile.api.products.getProducts.ProductFilterParams

class ProductsPagingSource(
    private val productsService: IProductsService,
    private val pagingParams: ProductFilterParams,
    private val pageSize: Int,
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 0
            val response = productsService.getProducts(
                page = page,
                category = pagingParams.category,
                key = pagingParams.key,
                size = pageSize,
                sort = pagingParams.sort,
            )
            if (response.isSuccess) {
                LoadResult.Page(
                    data = response.data!!.products,
                    prevKey = if (page == 0) null else page.minus(1),
                    nextKey =
                    if (((page + 1) * pageSize) + pageSize * 2 >= response.data.totalCount)
                        null
                    else
                        page + (params.loadSize / pageSize),
                )
            } else {
                LoadResult.Error(Exception(response.error!!.title))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}