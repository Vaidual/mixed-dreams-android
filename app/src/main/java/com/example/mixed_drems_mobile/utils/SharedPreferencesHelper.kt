package com.example.mixed_drems_mobile.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.mixed_drems_mobile.api.auth.TokensDto

object SharedPreferencesHelper {
    private const val PREFS_NAME = "AppPreferences"
    private const val ACCESS_TOKEN = "AccessToken"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveTokens(context: Context, tokens: TokensDto) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(ACCESS_TOKEN, tokens.accessToken)
        editor.apply()
    }

    fun getAccessToken(context: Context): String? {
        return getSharedPreferences(context).getString(ACCESS_TOKEN, null)
    }

    fun removeTokens(): Unit {
        val editor = getSharedPreferences(MainApplication.instance.applicationContext).edit()
        editor.remove(ACCESS_TOKEN)
        editor.apply()
    }

}