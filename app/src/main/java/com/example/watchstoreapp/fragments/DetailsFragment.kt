package com.example.watchstoreapp.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.watchstoreapp.Activities.FullScreenActivity
import com.example.watchstoreapp.IBadgeUpdater
import com.example.watchstoreapp.INavListener

import com.example.watchstoreapp.R
import com.example.watchstoreapp.databinding.FragmentDetailsBinding
import com.example.watchstoreapp.model.ProductItem
import com.example.watchstoreapp.utils.Constant
import com.example.watchstoreapp.viewModel.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.product_item.*

//@AndroidEntryPoint
class DetailsFragment : Fragment() {
    lateinit var listener: INavListener
    private val args: DetailsFragmentArgs by navArgs()
    lateinit var binding: FragmentDetailsBinding
    private var productCount:Int ?=1
    private var totalQuantity: Int?=1
    lateinit var rv:View
    private val storeViewModel: StoreViewModel by activityViewModels()
    lateinit var iBadgeUpdater:IBadgeUpdater
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            listener = activity as INavListener
            iBadgeUpdater = activity as IBadgeUpdater
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "error implementing")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        listener.showHideNavigations(false)
        rv = binding.root
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            val params = rv.layoutParams
            params.height = Constant.getUsableHeight(requireActivity())
            rv.layoutParams = params
        } catch (e: Exception) {
            Log.d("ListView1", "2 GroupInfoFragment Height Exception: $e")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPress()
        var product:ProductItem = args.productDetails
        totalQuantity = product.quantity
        binding.apply {
            title.text = product.name
            discountedPrice.text = "$"+product.offer
            mrp.text = "$"+product.price
            quantity.text = "1"
            desc.text = product.description
            Log.i("product.img",product.img.toString())
                Glide.with(watchImage.context).load(product.img).into(watchImage)
            if (product.isFavorite == true){
                binding.fav.setImageResource(R.drawable.fav_selected)
            }else{
                binding.fav.setImageResource(R.drawable.fav)
            }
        }
        binding.watchImage.setOnClickListener {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra("imgPath",product.img)
            requireActivity().startActivity(intent)
        }
        binding.back.setOnClickListener {
           callHomeFragment()

        }
        binding.fav.setOnClickListener {
            if (product.isFavorite == true){
                binding.fav.setImageResource(R.drawable.fav)
                product.isFavorite = false
            }else{
                binding.fav.setImageResource(R.drawable.fav_selected)
                product.isFavorite = true
            }
            storeViewModel.updateFav(product)

        }
        binding.plus.setOnClickListener {
            if (productCount!!<totalQuantity!!){
                productCount= productCount!! + 1
                binding.quantity.text = productCount.toString()
            }
            setTextColor()
        }
        binding.minus.setOnClickListener {
            if (productCount!!>1){
                productCount= productCount!! - 1
                binding.quantity.text = productCount.toString()
            }
            setTextColor()
        }

        binding.addToCart.setOnClickListener {
            product.quantity = productCount
            storeViewModel.addToCart(product)
            Toast.makeText(requireActivity(),"Product added successfully in Cart", Toast.LENGTH_SHORT).show()
            iBadgeUpdater.updateBadge()
        }


    }
    private fun setTextColor(){
        if(productCount==1){
            binding.minus.setTextColor(Color.GRAY)
            binding.plus.setTextColor(Color.WHITE)
        }else if(productCount == totalQuantity){
            binding.minus.setTextColor(Color.WHITE)
            binding.plus.setTextColor(Color.GRAY)
        }else{
            binding.plus.setTextColor(Color.WHITE)
            binding.minus.setTextColor(Color.WHITE)
        }
    }

    private fun callHomeFragment(){
        findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToHome())
    }

    private fun onBackPress(){
        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i("back pressed12","back pressed")
                callHomeFragment()

            }
        })
    }



}