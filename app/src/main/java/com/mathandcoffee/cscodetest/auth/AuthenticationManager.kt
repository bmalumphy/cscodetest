package com.mathandcoffee.cscodetest.auth

import com.mathandcoffee.cscodetest.rest.AuthenticationAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import javax.inject.Singleton

interface AuthenticationManager {
    suspend fun login(email: String, password: String): CompletableDeferred<String>
    fun revoke()
}

private class AuthenticationManagerImpl(
    private val authenticationAPIService: AuthenticationAPIService,
    private val authenticationDataManager: AuthenticationDataManager
): AuthenticationManager {

    override suspend fun login(email: String, password: String): CompletableDeferred<String> {
        val deferred = CompletableDeferred<String>()
        withContext(Dispatchers.IO) {
            val credentials = Credentials.basic(email, password)
            val response = authenticationAPIService.getUserToken(credentials)
            val body = response.body()
            if (body == null) {
                deferred.completeExceptionally(AuthenticationFailureException(1))
                return@withContext
            }
            val error = body.error
            val token = body.token
            if (token != null) {
                authenticationDataManager.saveCredentials(body.token)
                deferred.complete(body.token)
            } else {
                deferred.completeExceptionally(AuthenticationFailureException(error))
            }
        }
        return deferred
    }

    override fun revoke() {
        authenticationDataManager.clearCurrentCredentials()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationManagerProvider {

    @Provides
    @Singleton
    fun providesAuthenticationManager(
        authenticationDataManager: AuthenticationDataManager,
        authenticationAPIService: AuthenticationAPIService
    ): AuthenticationManager {
        return AuthenticationManagerImpl(authenticationAPIService, authenticationDataManager)
    }
}
