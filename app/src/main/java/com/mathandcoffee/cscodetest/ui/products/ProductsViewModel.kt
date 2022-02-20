package com.mathandcoffee.cscodetest.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathandcoffee.cscodetest.auth.AuthenticationDataManager
import com.mathandcoffee.cscodetest.auth.AuthenticationFailureException
import com.mathandcoffee.cscodetest.auth.AuthenticationManager
import com.mathandcoffee.cscodetest.rest.ProductAPIService
import com.mathandcoffee.cscodetest.rest.data.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productAPIService: ProductAPIService,
    private val authenticationDataManager: AuthenticationDataManager,
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>(listOf())
    val products: LiveData<List<Product>> = _products

    private var page = 0

    suspend fun pageProducts() {
        _products.value = withContext(Dispatchers.IO) {
            val authToken = authenticationDataManager.getCurrentCredentials() ?: return@withContext _products.value
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