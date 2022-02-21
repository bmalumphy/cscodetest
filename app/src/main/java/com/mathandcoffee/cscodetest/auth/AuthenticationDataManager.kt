package com.mathandcoffee.cscodetest.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface AuthenticationDataManager {
    fun saveCredentials(token: String)
    fun hasCurrentCredentials(): Boolean
    fun getCurrentCredentials(): String?
    fun clearCurrentCredentials()
}

class AuthenticationDataManagerImpl(
    private val sharedPreferences: SharedPreferences
): AuthenticationDataManager {

    private companion object {
        const val CREDENTIALS_KEY = "credentials"
    }

    override fun saveCredentials(token: String) {
        sharedPreferences.edit(commit = true) {
            this.putString(CREDENTIALS_KEY, "Bearer $token")
        }
    }

    override fun hasCurrentCredentials(): Boolean {
        return sharedPreferences.contains(CREDENTIALS_KEY)
    }

    override fun getCurrentCredentials(): String? {
        return sharedPreferences.getString(CREDENTIALS_KEY, null)
    }

    override fun clearCurrentCredentials() {
        sharedPreferences.edit(commit = true) {
            this.remove(CREDENTIALS_KEY)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationDataManagerProvider {

    @Provides
    @Singleton
    fun provideAuthenticationDataManager(
        sharedPreferences: SharedPreferences
    ): AuthenticationDataManager {
        return AuthenticationDataManagerImpl(sharedPreferences)
    }
}
