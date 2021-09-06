package com.example.watchstoreapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watchstoreapp.R
import com.example.watchstoreapp.databinding.CartItemBinding

import com.example.watchstoreapp.model.CarttItem

class CartAdapter(private val listener: IcartListener) :RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private var cartList:ArrayList<CarttItem> = ArrayList<CarttItem>()
    var totalPrice: Int = 0
     var totalDiscount: Int = 0
    class CartViewHolder(val binding: CartItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
       val product = cartList[position]
        val dataArray = calculatePrice(product.price!!, product.offer!!, product.quantity!!)
        holder.binding.apply {
           title.text = product.name
            quantity.text = "Qty: "+ product.quantity
            discountedPrice.text = "$"+dataArray[1]
            mrp.text = "$"+dataArray[0]
            Glide.with(holder.itemView.context).load(product.img).into(productImage)
        }
        holder.binding.delete.setOnClickListener {
            listener.onCarttItemClicked(product)
        }

    }

    override fun getItemCount()= cartList.size

    private fun calculatePrice(price:String, offer:String, quantity:Int):Array<Int>{
        val mrp = (price?.toInt() ?: 0) * quantity!!
        val discountedPrice = (offer?.toInt() ?: 0) * quantity
        totalPrice += mrp
        totalDiscount += discountedPrice
        Log.i("totalPrice1", totalPrice.toString())
        Log.i("totalDiscount1", totalDiscount.toString())
        return arrayOf(mrp, discountedPrice)

    }
    fun getPriceData():Array<Int>{
        Log.i("totalPrice2", totalPrice.toString())
        Log.i("totalDiscount2", totalDiscount.toString())
        return arrayOf(totalPrice, totalDiscount)
    }
    private val diffCallback = object: DiffUtil.ItemCallback<CarttItem>(){
        override fun areItemsTheSame(oldItem: CarttItem, newItem: CarttItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarttItem, newItem: CarttItem): Boolean {
            return oldItem == newItem
        }

    }

//    private val differ = AsyncListDiffer(this, diffCallback)
//    var cartList: ArrayList<CarttItem>
//    get() = differ.currentList as ArrayList<CarttItem>
//    set(value) {differ.submitList(value)}
    fun updateList(newList: ArrayList<CarttItem>){
        cartList.clear()
        cartList = newList
        notifyDataSetChanged()
    }


}

interface IcartListener{
    fun onCarttItemClicked(product: CarttItem)
}