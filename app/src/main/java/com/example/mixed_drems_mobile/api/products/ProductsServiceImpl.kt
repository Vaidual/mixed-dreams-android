package com.example.mixed_drems_mobile.api.products

import android.content.Context
import com.example.mixed_drems_mobile.api.ApiClient
import com.example.mixed_drems_mobile.api.ApiResponse
import com.example.mixed_drems_mobile.api.ApiRoutes
import com.example.mixed_drems_mobile.api.postWithApiResponse
import com.example.mixed_drems_mobile.api.products.getProducts.GetProductsResponse
import com.example.mixed_drems_mobile.utils.SharedPreferencesHelper
import io.ktor.client.request.headers
import io.ktor.http.HttpMethod
import io.ktor.http.takeFrom

class ProductsServiceImpl(
    private val context: Context
) : IProductsService {
    override suspend fun getProducts(
        page: Int?,
        size: Int?,
        key: String?,
        category: String?,
        sort: String?,
    ): ApiResponse<GetProductsResponse> {
        return ApiClient.client.postWithApiResponse {
            url {

                takeFrom(ApiRoutes.Products)
                if (page != null) parameters.append("page", page.toString())
                if (size != null) parameters.append("size", size.toString())
                if (key != null) parameters.append("key", key)
                if (category != null) parameters.append("category", category)
                if (sort != null) parameters.append("sort", sort)
            }
            method = HttpMethod.Get
            headers {
                append("Authorization", "Bearer ${SharedPreferencesHelper.getAccessToken(context)}")
            }
        }
    }

}