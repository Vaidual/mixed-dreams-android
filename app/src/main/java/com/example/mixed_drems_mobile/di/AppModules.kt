package com.example.mixed_drems_mobile.di

import com.example.mixed_drems_mobile.api.auth.AuthRepositoryImpl
import com.example.mixed_drems_mobile.api.auth.IAuthRepository
import com.example.mixed_drems_mobile.api.orders.IOrdersRepository
import com.example.mixed_drems_mobile.api.orders.OrdersRepositoryImpl
import com.example.mixed_drems_mobile.api.products.IProductsRepository
import com.example.mixed_drems_mobile.api.products.ProductsRepositoryImpl
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
    fun provideProductsService(): IProductsRepository = ProductsRepositoryImpl()

    @Provides
    @Singleton
    fun provideAuthService(): IAuthRepository = AuthRepositoryImpl()

    @Provides
    @Singleton
    fun provideOrdersRepo(): IOrdersRepository = OrdersRepositoryImpl()
}