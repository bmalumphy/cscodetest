package com.mathandcoffee.cscodetest.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mathandcoffee.cscodetest.R
import com.mathandcoffee.cscodetest.databinding.ProductsFragmentBinding
import com.mathandcoffee.cscodetest.ui.login.LoginFragment
import com.mathandcoffee.cscodetest.ui.products.recycler_view.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var binding: ProductsFragmentBinding

    private val adapter = ProductsAdapter()

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProductsFragmentBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(scrollListener)

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigation_host, LoginFragment())
            transaction?.commit()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.products.observe(viewLifecycleOwner, { products ->
            isLoading = false
            adapter.differ.submitList(products)
        })

        lifecycleScope.launch {
            viewModel.pageProducts()
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            val shouldPaginate = lastVisibleItemPosition == totalItemCount - 5 && !isLoading
            if(shouldPaginate) {
                isLoading = true
                lifecycleScope.launch {
                    viewModel.pageProducts()
                }
            }
        }
    }
}