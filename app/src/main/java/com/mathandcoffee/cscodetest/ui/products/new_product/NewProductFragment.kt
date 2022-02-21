package com.mathandcoffee.cscodetest.ui.products.new_product

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mathandcoffee.cscodetest.R
import com.mathandcoffee.cscodetest.databinding.FragmentNewProductBinding
import com.mathandcoffee.cscodetest.databinding.ProductsFragmentBinding
import com.mathandcoffee.cscodetest.rest.data.Product
import com.mathandcoffee.cscodetest.ui.products.ProductsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

@AndroidEntryPoint
class NewProductFragment: Fragment() {
    private val viewModel: NewProductViewModel by viewModels()
    private lateinit var binding: FragmentNewProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewProductBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            back()
        }

        binding.createProductButton.setOnClickListener {
            
            // Given that the API never returns a 4xx response, we can make a blank product.
            // Given more time I would catch the failure cases here, but this shouldn't ever crash at least.
            val name = binding.productNameEditText.text.toString()
            val description = binding.descriptionTextView.text.toString()
            val style = binding.styleEditText.text.toString()
            val brand = binding.brandEditText.text.toString()
            try {
                val pricing = binding.shippingPriceEditText.text.toString().toInt()
                viewModel.createNewProduct(name, description, style, brand, pricing)
            } catch (exception: NumberFormatException) {
                showCorrectionDialog("Improper Number Format for Price", "Please enter it as an Integer (Not Decimal)")
            }
        }

        viewModel.events.flowWithLifecycle(lifecycle).onEach {
            when(it) {
                is NewProductViewModel.Event.ProductCreated -> back()
            }
        }.launchIn(lifecycleScope)

        return binding.root
    }

    private fun back() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.navigation_host, ProductsFragment())
        transaction?.disallowAddToBackStack()
        transaction?.commit()
    }

    private fun showCorrectionDialog(title: String, message: String) {
        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}