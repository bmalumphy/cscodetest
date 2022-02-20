package com.mathandcoffee.cscodetest.ui.products.recycler_view

import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.set
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mathandcoffee.cscodetest.databinding.ProductCellBinding
import com.mathandcoffee.cscodetest.rest.data.Product

class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ProductCellBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        with(product) {
            var name = this.productName
            if (name.isEmpty()) {
                name = "Nameless Product"
            }

            var price = "$${this.shippingPrice}.00"
            if (this.shippingPrice == "0") {
                price = "FREE"
            }
            val priceText = SpannableString("$name    $price")
            priceText.setSpan(RelativeSizeSpan(0.75f), this.productName.length, priceText.length, 0)
            holder.binding.productName.setText(priceText, TextView.BufferType.SPANNABLE)
            holder.binding.productDescription.text = this.description
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}