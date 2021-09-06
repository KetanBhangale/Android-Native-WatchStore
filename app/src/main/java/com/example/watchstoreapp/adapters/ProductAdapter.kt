package com.example.watchstoreapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watchstoreapp.R
import com.example.watchstoreapp.databinding.ProductItemBinding
import com.example.watchstoreapp.model.ProductItem

class ProductAdapter(private val listener:IProductListener):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var productList:ArrayList<ProductItem> = ArrayList<ProductItem>()
    class ProductViewHolder(val binding: ProductItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
       val product = productList[position]
        holder.binding.apply {
            discountedPrice.text = "$"+product.offer
            mrp.text = "$"+product.price
            title.text = product.name
            Glide.with(holder.itemView.context).load(product.img).into(productImage)
            if (product.isFavorite == true){
               holder.binding.fav.setImageResource(R.drawable.fav_selected)
            }else{
                holder.binding.fav.setImageResource(R.drawable.fav)
            }
        }
        holder.binding.productImage.setOnClickListener {
            listener.onProductItemClicked(product)
        }
    }

    override fun getItemCount()= productList.size

    private val diffCallback = object: DiffUtil.ItemCallback<ProductItem>(){
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem == newItem
        }

    }

//    private val differ = AsyncListDiffer(this, diffCallback)
//    var productList: ArrayList<ProductItem>
//    get() = differ.currentList as ArrayList<ProductItem>
//    set(value) {differ.submitList(value)}
    fun updateList(newList: ArrayList<ProductItem>){
        productList.clear()
        productList = newList
        notifyDataSetChanged()
    }


}

interface IProductListener{
    fun onProductItemClicked(product:ProductItem)
}