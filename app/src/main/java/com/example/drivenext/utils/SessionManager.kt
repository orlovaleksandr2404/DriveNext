package com.example.drivenext.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "drivenext_session"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_DRIVER_LICENSE = "driver_license"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveAuthSession(context: Context, email: String, token: String) {
        getPrefs(context).edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    fun saveDriverProfile(context: Context, fullName: String, licenseNumber: String) {
        getPrefs(context).edit()
            .putString(KEY_USER_NAME, fullName)
            .putString(KEY_DRIVER_LICENSE, licenseNumber)
            .apply()
    }

    fun getUserName(context: Context): String {
        return getPrefs(context).getString(KEY_USER_NAME, "Иван Иванов") ?: "Иван Иванов"
    }

    fun getDriverLicense(context: Context): String {
        return getPrefs(context).getString(KEY_DRIVER_LICENSE, "45164634") ?: "45164634"
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_EMAIL, null)
    }

    fun clearSession(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}