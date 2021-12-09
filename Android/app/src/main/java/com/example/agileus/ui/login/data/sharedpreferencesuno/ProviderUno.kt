package com.example.agileus.ui.login.data.sharedpreferencesuno

import android.content.Context
import android.content.SharedPreferences

class ProviderUno () {
    companion object {
        lateinit var preferences: SharedPreferences

        fun initialize(context: Context) {
            preferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        }

        fun setUserPass(userName: String?, pass: String) {
            preferences.edit().putString("username", userName).apply()
            preferences.edit().putString("pass", pass).apply()
        }

        fun getUserName(): String? {
            return preferences.getString("username", "A")
        }

        fun getPassword(): String? {
            return preferences.getString("pass", "a")
        }
    }
}