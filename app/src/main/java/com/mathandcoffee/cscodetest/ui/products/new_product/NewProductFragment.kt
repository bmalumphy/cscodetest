package com.mathandcoffee.cscodetest.ui.products.new_product

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewProductFragment: Fragment() {
    val viewModel: NewProductViewModel by viewModels()
}