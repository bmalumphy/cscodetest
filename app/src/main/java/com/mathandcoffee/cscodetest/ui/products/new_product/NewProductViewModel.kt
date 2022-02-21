package com.mathandcoffee.cscodetest.ui.products.new_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathandcoffee.cscodetest.ui.products.ProductUpdateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val productUpdateManager: ProductUpdateManager
) : ViewModel() {

    sealed class Event {
        object ProductCreated : Event()
    }

    private var productCreatedJob: Job? = null

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        productCreatedJob = viewModelScope.launch {
            productUpdateManager.itemWasCreated.collect {
                _events.send(Event.ProductCreated)
            }
        }
    }

    fun createNewProduct(
        name: String,
        description: String,
        style: String,
        brand: String,
        shippingPriceCents: Int
    ) {
        viewModelScope.launch {
            productUpdateManager.createProduct(name, description, style, brand, shippingPriceCents)
        }
    }
}