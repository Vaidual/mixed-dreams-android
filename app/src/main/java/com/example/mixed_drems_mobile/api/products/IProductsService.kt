package com.example.mixed_drems_mobile.api.products

import com.example.mixed_drems_mobile.models.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IProductsService {

    @GET("/products/{id}")
    suspend fun getProduct(@Path("id") id:String): Response<Product>
}