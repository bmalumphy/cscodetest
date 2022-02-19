package com.mathandcoffee.cscodetest.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesProvider {

    private const val ENCRYPTED_SHARED_PREFERENCES = "MATH_AND_COFFEE_ENCRYPTED_SHARED_PREFERENCES"

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(appContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val keyEncryptionScheme = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
        val valueEncryptionScheme = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM

        return EncryptedSharedPreferences.create(
            appContext,
            ENCRYPTED_SHARED_PREFERENCES,
            masterKey,
            keyEncryptionScheme,
            valueEncryptionScheme
        )
    }
}