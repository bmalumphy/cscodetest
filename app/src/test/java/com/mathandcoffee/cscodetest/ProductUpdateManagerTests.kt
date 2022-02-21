package com.mathandcoffee.cscodetest

import app.cash.turbine.test
import com.mathandcoffee.cscodetest.auth.AuthenticationManager
import com.mathandcoffee.cscodetest.rest.ProductAPIService
import com.mathandcoffee.cscodetest.rest.data.Product
import com.mathandcoffee.cscodetest.rest.requests.NewProductRequest
import com.mathandcoffee.cscodetest.rest.responses.DeleteProductResponse
import com.mathandcoffee.cscodetest.rest.responses.GetProductResponse
import com.mathandcoffee.cscodetest.rest.responses.NewProductResponse
import com.mathandcoffee.cscodetest.ui.products.ProductUpdateManager
import com.mathandcoffee.cscodetest.ui.products.ProductUpdateManagerImpl
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class ProductUpdateManagerTests {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private fun getTestObject(
        authManager: AuthenticationManager? = null,
        productAPIService: ProductAPIService? = null
    ): ProductUpdateManager {
        val am = authManager ?: mockk(relaxed = true)
        val apiService = productAPIService ?: mockk(relaxed = true)

        return ProductUpdateManagerImpl(am, apiService, testDispatcher)
    }

    @Test
    fun `test delete product returns valid deleted id`() = testScope.runTest {

        val authManager: AuthenticationManager = mockk(relaxed = true)
        every { authManager.currentToken() } returns "token"

        val apiService: ProductAPIService = mockk(relaxed = true)
        coEvery { apiService.deleteProduct(0, "token") } returns Response.success(
            DeleteProductResponse("Deleted successfully", 0)
        )
        val updateManager = getTestObject(authManager, apiService)

        updateManager.itemWasDeletedWithId.test {
            updateManager.deleteProduct(0)
            awaitItem().shouldBe(0)
        }
    }

    @Test
    fun `test create product returns valid created product`() = testScope.runTest {

        val authManager: AuthenticationManager = mockk(relaxed = true)
        every { authManager.currentToken() } returns "token"

        val newProductRequest = NewProductRequest("badKey", "Nike Sneakers", "Nike shoes", "Running Shoes", "Nike", 700)
        val apiService: ProductAPIService = mockk(relaxed = true)
        coEvery { apiService.createProduct("token", newProductRequest) } returns Response.success(
            NewProductResponse("Product created", 0)
        )

        val product = Product(
            0,
            newProductRequest.name,
            newProductRequest.description,
            newProductRequest.style,
            newProductRequest.brand,
            null,
            newProductRequest.shippingPriceCents,
            null
        )
        coEvery { apiService.getProduct(0, "token") } returns Response.success(
            GetProductResponse(product)
        )
        val updateManager = getTestObject(authManager, apiService)

        updateManager.itemWasCreated.test {
            updateManager.createProduct(newProductRequest.name, newProductRequest.description, newProductRequest.style, newProductRequest.brand, newProductRequest.shippingPriceCents)
            awaitItem().shouldBe(product)
        }
    }
}