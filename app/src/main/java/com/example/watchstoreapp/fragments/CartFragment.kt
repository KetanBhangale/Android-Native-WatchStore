package com.example.watchstoreapp.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchstoreapp.IBadgeUpdater
import com.example.watchstoreapp.INavListener
import com.example.watchstoreapp.R
import com.example.watchstoreapp.adapters.CartAdapter
import com.example.watchstoreapp.adapters.CategoryAdapter
import com.example.watchstoreapp.adapters.IcartListener
import com.example.watchstoreapp.databinding.FragmentCartBinding
import com.example.watchstoreapp.databinding.FragmentHomeBinding
import com.example.watchstoreapp.model.CarttItem
import com.example.watchstoreapp.utils.Constant
import com.example.watchstoreapp.viewModel.StoreViewModel
import kotlinx.coroutines.*

class CartFragment : Fragment(), IcartListener {
    private val storeViewModel: StoreViewModel by activityViewModels()
    lateinit var binding: FragmentCartBinding
    lateinit var rv:View
    lateinit var cartAdapter: CartAdapter
    private var cartItemList: ArrayList<CarttItem> =ArrayList()
    lateinit var iBadgeUpdater: IBadgeUpdater

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            iBadgeUpdater = activity as IBadgeUpdater
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "error implementing")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)

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
        setUpLayout()
        setupCartRV()
        loadData()
        iBadgeUpdater.updateBadge()
        binding.placeOrder.setOnClickListener {
            MainScope().launch {
                storeViewModel.deleteAllCartItems(cartItemList)
                Log.i("seqeuntail call","sec")
                delay(800)
                iBadgeUpdater.updateBadge()
                findNavController().navigate(CartFragmentDirections.actionCartToSuccessFragment())
            }

        }

    }

    private fun loadData() {
        storeViewModel.getCartItems()
        GlobalScope.launch(Dispatchers.Main) {
            delay(800)
            storeViewModel.cartList.observe(requireActivity(), Observer { list ->
                Log.i("list size12", list.size.toString())
                cartItemList = list
                cartAdapter.updateList(list)
                setUpLayout()
            })
            delay(800)
            val priceData = cartAdapter.getPriceData()
            setPrice(priceData)
        }
    }

    private fun setUpLayout(){
        Log.i("cartItemList",cartItemList.size.toString())
        if (cartItemList.size>0){
            binding.rl.visibility = View.VISIBLE
            binding.placeHolder.visibility = View.GONE
            binding.myCart.visibility = View.VISIBLE
        }else{
            binding.placeHolder.visibility = View.VISIBLE
            binding.rl.visibility = View.GONE
            binding.myCart.visibility = View.GONE
        }
    }
    private fun setPrice(priceData: Array<Int>){
        val mrp:String = priceData[0].toString()
        val discount: String = (priceData[0] - priceData[1]).toString()
        val charges:String = String.format("%.2f",(priceData[1] * 0.05))
        val netPayable:String =  (priceData[1] +(priceData[1] * 0.05)).toString()
        Log.i("totalPrice3", mrp)
        Log.i("totalDiscount3", discount)
        binding.price.text = "$"+mrp
        binding.discount.text = "-$"+discount
        binding.charges.text = "$"+ charges
        binding.totalPrice.text = "$"+netPayable

    }
    private fun setupCartRV() {
        cartAdapter = CartAdapter(this)
        binding.cartRv.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            hasFixedSize()
            adapter = cartAdapter
        }
    }

    override fun onCarttItemClicked(product:CarttItem) {
        storeViewModel.deleteCartItem(product.id.toString())
        GlobalScope.launch(Dispatchers.Main) {
            delay(800)
            val id = findNavController().currentDestination?.id
            findNavController().popBackStack(id!!,true)
            findNavController().navigate(id)
        }

//        GlobalScope.launch(Dispatchers.Main) {
//            storeViewModel.deleteCartItem(product.id.toString())

//            storeViewModel.getCartItems()
//            delay(800)
//            storeViewModel.getCartItems()
//            storeViewModel.cartList.observe(requireActivity(), Observer { list ->
//                Log.i("list size1234", list.size.toString())
//                cartItemList = list
//                cartAdapter.updateList(list)
//                //setUpLayout()
//            })

//        }
//        cartItemList.remove(product)
//        Log.i("size",cartItemList.size.toString())
//        cartAdapter.updateList(cartItemList)


    }

}