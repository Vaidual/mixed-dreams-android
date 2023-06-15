package com.example.mixed_drems_mobile.utils

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mixed_drems_mobile.models.CartItem
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    companion object {
        lateinit var instance: MainApplication
            private set
    }

    val shoppingCart = ShoppingCart()


    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

class ShoppingCart() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    fun incrementItem(id: String) {
        val index = _cartItems.indexOfFirst { it.id == id }
        if (index == -1) return
        _cartItems[index] = _cartItems[index].let {
            it.copy(count = it.count + 1)
        }
    }

    fun addItem(item: CartItem) {
        val existingItem = _cartItems.find { it.id == item.id }
        if (existingItem != null)
            existingItem.count += item.count
        else
            _cartItems.add(item)
    }

    fun decrementItem(id: String) {
        val index = _cartItems.indexOfFirst { it.id == id }
        if (index == -1) return
        _cartItems[index] = _cartItems[index].let {
            it.copy(count = it.count - 1)
        }
    }

    fun removeItem(id: String) {
        _cartItems.remove(_cartItems.find { it.id == id })
    }
}