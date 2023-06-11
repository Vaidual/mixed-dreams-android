package com.example.mixed_drems_mobile

import com.example.mixed_drems_mobile.api.products.IProductsService
import com.example.mixed_drems_mobile.api.products.ProductsServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModules {

    @Provides
    @Singleton
    fun provideGamesRepository(): IProductsService = ProductsServiceImpl()
}