package com.example.watchstoreapp.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.watchstoreapp.databinding.CategoryItemBinding
import com.example.watchstoreapp.model.CategoryItem

class CategoryAdapter(private val listener: ICategoryListener):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var categoryList:ArrayList<CategoryItem> = ArrayList<CategoryItem>()
    var row_index=-1
    class CategoryViewHolder(val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val viewHolder = CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
       val category = categoryList[position]

        holder.binding.title.text = category.name
        holder.binding.title.setOnClickListener {
            row_index = position
            //holder.binding.title.setTextColor(Color.parseColor("#c85a54"))
            notifyDataSetChanged()
            listener.onCategoryClick(category)

        }
        Log.i("row index", row_index.toString())
        if (row_index==position){
            holder.binding.title.setTextColor(Color.parseColor("#c85a54"))
        }else{
            holder.binding.title.setTextColor(Color.parseColor("#000000"))
        }
    }

    override fun getItemCount()= categoryList.size

    private val diffCallback = object: DiffUtil.ItemCallback<CategoryItem>(){
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }

    }

//    private val differ = AsyncListDiffer(this, diffCallback)
//    var categoryList: ArrayList<CategoryItem>
//    get() = differ.currentList as ArrayList<CategoryItem>
//    set(value) {differ.submitList(value)}
    fun updateList(newList: ArrayList<CategoryItem>){
        categoryList.clear()
        categoryList = newList
    notifyDataSetChanged()
    }


}
interface ICategoryListener{
    fun onCategoryClick(category:CategoryItem)
}