package com.example.agileus.ui.login.repository

import android.content.Context
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.ui.login.data.model.Users
import java.security.Provider
import android.preference.PreferenceManager

import android.content.SharedPreferences
import com.example.agileus.ui.login.data.model.UserUno
import com.example.agileus.ui.login.data.sharedpreferencesuno.ProviderUno
import com.example.agileus.utils.Constantes
import com.google.android.gms.common.internal.Constants


class Repository {

    private val sharedPreferences = ProviderUno

    fun addUser(user: UserUno) {
        sharedPreferences.setUserPass(user.username, user.pass)
    }

    fun getUserName() = sharedPreferences.getUserName()
    fun getPassword() = sharedPreferences.getPassword()

}