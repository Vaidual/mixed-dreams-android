package com.example.mixed_drems_mobile.pages.products

import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mixed_drems_mobile.api.products.Product

//class ProductAdapter : PagingDataAdapter<Product, ProductAdapter.Holder>(ProductsDiffCallback) {
//
//    override fun onBindViewHolder(holder: Holder, position: Int) {
//        val product = getItem(position) ?: return
//        with (holder.binding) {
//            nameTextView.text = product.name
//            userCompanyTextView.text = product.company
//            loadProductPhoto(photoImageView, product.primaryImage)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ItemUserBinding.inflate(inflater, parent, false)
//        return Holder(binding)
//    }
//
//    private fun loadProductPhoto(imageView: ImageView, url: String) {
//        val context = imageView.context
//        if (url.isNotBlank()) {
//            Glide.with(context)
//                .load(url)
//                .circleCrop()
//                .placeholder(R.drawable.ic_user_avatar)
//                .error(R.drawable.ic_user_avatar)
//                .into(imageView)
//        } else {
//            Glide.with(context)
//                .load(R.drawable.ic_user_avatar)
//                .into(imageView)
//        }
//    }
//
//    class Holder(
//        val binding: ItemUserBinding
//    ) : RecyclerView.ViewHolder(binding.root)
//}
//
//class ProductsDiffCallback : DiffUtil.ItemCallback<Product>() {
//    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return oldItem == newItem
//    }
//}