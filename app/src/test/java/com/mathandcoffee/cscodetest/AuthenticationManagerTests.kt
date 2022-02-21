package com.mathandcoffee.cscodetest

import android.content.SharedPreferences
import com.mathandcoffee.cscodetest.auth.AuthenticationDataManager
import com.mathandcoffee.cscodetest.auth.AuthenticationDataManagerImpl
import com.mathandcoffee.cscodetest.auth.AuthenticationManager
import com.mathandcoffee.cscodetest.auth.AuthenticationManagerImpl
import com.mathandcoffee.cscodetest.rest.AuthenticationAPIService
import com.mathandcoffee.cscodetest.rest.responses.AuthTokenResponse
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.Credentials
import org.junit.jupiter.api.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthenticationManagerTests {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private fun getTestObject(
        dataManager: AuthenticationDataManager? = null,
        authenticationAPIService: AuthenticationAPIService? = null
    ): AuthenticationManager {
        val dm = dataManager ?: mockk(relaxed = true)
        val apiService = authenticationAPIService ?: mockk(relaxed = true)

        return AuthenticationManagerImpl(apiService, dm, testDispatcher)
    }

    @Test
    fun `test Logged In Token Persists`() {
        val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
        every { mockSharedPreferences.contains(any()) } returns true
        every { mockSharedPreferences.getString(any(), any()) } returns "token"
        val dataManager = AuthenticationDataManagerImpl(mockSharedPreferences)
        val authenticationManager = getTestObject(dataManager)
        authenticationManager.hasCurrentCredentials().shouldBeTrue()
        authenticationManager.currentToken().shouldBe("token")
    }

    @Test
    fun `test unauthenticated user has no token`() {
        val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
        every { mockSharedPreferences.contains(any()) } returns false
        every { mockSharedPreferences.getString(any(), any()) } returns null
        val dataManager = AuthenticationDataManagerImpl(mockSharedPreferences)
        val authenticationManager = getTestObject(dataManager)
        authenticationManager.hasCurrentCredentials().shouldBeFalse()
        authenticationManager.currentToken().shouldBe(null)
    }

    @Test
    fun `test login creates token and can pull it from data manager`() = testScope.runTest {
        val mockAPIService: AuthenticationAPIService = mockk(relaxed = true)
        val email = "asdf@gmail.com"
        val password = "asdfasdf"
        val credentials = Credentials.basic(email, password)
        coEvery { mockAPIService.getUserToken(credentials) } returns Response.success(
            AuthTokenResponse("token", 0)
        )
        val authenticationManager = getTestObject(authenticationAPIService = mockAPIService)
        authenticationManager.login(email, password).await().shouldBe("token")
    }
}