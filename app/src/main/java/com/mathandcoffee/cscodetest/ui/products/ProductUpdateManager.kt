package com.mathandcoffee.cscodetest.ui.products

import com.mathandcoffee.cscodetest.auth.AuthenticationManager
import com.mathandcoffee.cscodetest.rest.ProductAPIService
import com.mathandcoffee.cscodetest.rest.data.Product
import com.mathandcoffee.cscodetest.rest.requests.NewProductRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Singleton

interface ProductUpdateManager {

    val itemWasDeletedWithId: SharedFlow<Int>
    val itemWasCreated: SharedFlow<Product>

    suspend fun getProduct(id: Int): CompletableDeferred<Product?>
    suspend fun createProduct(
        name: String,
        description: String,
        style: String,
        brand: String,
        shippingPriceCents: Int
    )
    suspend fun deleteProduct(id: Int)
}

class ProductUpdateManagerImpl(
    private val authenticationManager: AuthenticationManager,
    private val productAPIService: ProductAPIService,
    private val dispatcher: CoroutineDispatcher
): ProductUpdateManager {

    private val _itemWasDeletedWithId = MutableSharedFlow<Int>()
    override val itemWasDeletedWithId: SharedFlow<Int> = _itemWasDeletedWithId

    private val _itemWasCreated = MutableSharedFlow<Product>()
    override val itemWasCreated: SharedFlow<Product> = _itemWasCreated

    override suspend fun getProduct(id: Int): CompletableDeferred<Product?> {
        val deferred = CompletableDeferred<Product?>()
        withContext(dispatcher) {
            val authToken = authenticationManager.currentToken()
            if (authToken == null) {
                deferred.complete(null)
                return@withContext
            }
            deferred.complete(productAPIService.getProduct(id, authToken).body()?.product)
        }
        return deferred
    }

    override suspend fun createProduct(
        name: String,
        description: String,
        style: String,
        brand: String,
        shippingPriceCents: Int
    ) {
        val authToken = authenticationManager.currentToken() ?: return
        withContext(dispatcher) {
            val response = productAPIService.createProduct(
                authToken,
                NewProductRequest("badKey", name, description, style, brand, shippingPriceCents))
            val id = response.body()?.productId ?: return@withContext
            val product = getProduct(id).await() ?: return@withContext
            _itemWasCreated.emit(product)
        }
    }

    override suspend fun deleteProduct(id: Int) {
        val authToken = authenticationManager.currentToken() ?: return
        withContext(dispatcher) {
            val response = productAPIService.deleteProduct(id, authToken)
            if (!response.isSuccessful) {
                Timber.tag("PRODUCTUPDATESERVICE").e("Received bad response from server with error: ${response.errorBody()}")
                return@withContext
            }
            val deletedId = response.body()?.productId ?: return@withContext
            _itemWasDeletedWithId.emit(deletedId)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ProductUpdateManagerProvider {

    @Provides
    @Singleton
    fun provideProductUpdateManager(
        authenticationManager: AuthenticationManager,
        productAPIService: ProductAPIService
    ): ProductUpdateManager {
        return ProductUpdateManagerImpl(authenticationManager, productAPIService, Dispatchers.IO)
    }
}
