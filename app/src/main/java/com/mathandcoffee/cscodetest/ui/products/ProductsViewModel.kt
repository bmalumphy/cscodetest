package com.mathandcoffee.cscodetest.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathandcoffee.cscodetest.auth.AuthenticationManager
import com.mathandcoffee.cscodetest.rest.ProductAPIService
import com.mathandcoffee.cscodetest.rest.data.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productUpdateManager: ProductUpdateManager,
    private val productAPIService: ProductAPIService,
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>(listOf())
    val products: LiveData<List<Product>> = _products

    private var page = 0

    private var productCreationJob: Job? = null
    private var productDeletionJob: Job? = null

    init {
        productCreationJob = viewModelScope.launch {
            productUpdateManager.itemWasCreated.collect { product ->
                _products.value = _products.value?.toMutableList().apply {
                    this?.add(0, product)
                }
            }
        }

        productDeletionJob = viewModelScope.launch {
            productUpdateManager.itemWasDeletedWithId.collect { id ->
                _products.value = _products.value?.toMutableList().apply {
                    this?.removeAll { it.id == id }
                }
            }
        }
    }

    suspend fun createRandomProduct() {
        productUpdateManager.createProduct("Bryans Test", "This is a test", "Ambidextrous", "IHOP", 100)
    }

    suspend fun deleteProduct(id: Int) {
        productUpdateManager.deleteProduct(id)
    }

    suspend fun pageProducts() {
        _products.value = withContext(Dispatchers.IO) {
            val authToken = authenticationManager.currentToken() ?: return@withContext _products.value
            val response = productAPIService.getProducts(authToken, page, 20)
            val body = response.body() ?: return@withContext _products.value
            page++
            _products.value?.plus(body.products)
        }!!
    }

    fun logout() {
        // Future Efforts: Currently there is not a way to revoke a JWT on the API
        authenticationManager.revoke()
    }
}