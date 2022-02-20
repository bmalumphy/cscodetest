package com.mathandcoffee.cscodetest.ui.products.new_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathandcoffee.cscodetest.auth.AuthenticationDataManager
import com.mathandcoffee.cscodetest.rest.ProductAPIService
import com.mathandcoffee.cscodetest.rest.requests.NewProductRequest
import com.mathandcoffee.cscodetest.ui.products.ProductUpdateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val productUpdateManager: ProductUpdateManager
) : ViewModel() {

    suspend fun createNewProduct(
        name: String,
        description: String,
        style: String,
        brand: String,
        shippingPriceCents: Int
    ) {
        productUpdateManager.createProduct(name, description, style, brand, shippingPriceCents)
    }
}