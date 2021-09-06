package com.example.watchstoreapp.fragments

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchstoreapp.IBadgeUpdater
import com.example.watchstoreapp.INavListener

import com.example.watchstoreapp.adapters.CategoryAdapter
import com.example.watchstoreapp.adapters.ICategoryListener
import com.example.watchstoreapp.adapters.IProductListener
import com.example.watchstoreapp.adapters.ProductAdapter
import com.example.watchstoreapp.databinding.FragmentHomeBinding
import com.example.watchstoreapp.model.CategoryItem
import com.example.watchstoreapp.model.ProductItem
import com.example.watchstoreapp.utils.Constant
import com.example.watchstoreapp.viewModel.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//@AndroidEntryPoint
class HomeFragment : Fragment(), ICategoryListener, IProductListener {
    lateinit var listener: INavListener
    private val storeViewModel: StoreViewModel by activityViewModels()
    lateinit var catAdapter: CategoryAdapter
    lateinit var productAdapter: ProductAdapter
    lateinit var binding:FragmentHomeBinding
    lateinit var rv:View
    lateinit var iBadgeUpdater: IBadgeUpdater
    lateinit var offerInstance: ProductItem
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
       //val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        listener.showHideNavigations(true)
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
        setupCategoryRV()
        setupProductRV()
        storeViewModel.getAllCategories()
        storeViewModel.getProductByCategory("1")
        //delay(3000)
        GlobalScope.launch(Dispatchers.Main) {
            delay(800)
            storeViewModel.catList.observe(requireActivity(), Observer { list->
                Log.i("list size", list.size.toString())
                catAdapter.updateList(list)
            })
            storeViewModel.productList.observe(requireActivity(), Observer { list->
                Log.i("product list size", list.size.toString())
                productAdapter.updateList(list)
            })


        }

        iBadgeUpdater.updateBadge()
        createOfferInstance()
        binding.offerButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetailsFragment(offerInstance))
        }
//        Log.i("binding.categoryRv.visibility",binding.categoryRv.visibility.toString())
//        Log.i("binding.productListRv.visibility",binding.productListRv.visibility.toString())
    }



//    override fun onResume() {
//        super.onResume()
//        Log.i("onResume","onResume")
//        Log.i("Height",Constant.getUsableHeight(requireActivity()).toString())
//        catAdapter.notifyDataSetChanged()
//        productAdapter.notifyDataSetChanged()
//    }


    private fun setupCategoryRV() {
        catAdapter = CategoryAdapter(this)
        binding.categoryRv.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            hasFixedSize()
            adapter = catAdapter
        }
    }
    private fun setupProductRV() {
        productAdapter = ProductAdapter(this)
        binding.productListRv.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            hasFixedSize()
            adapter = productAdapter
        }
    }
    override fun onCategoryClick(category: CategoryItem) {
        storeViewModel.getProductByCategory(category.id!!)
    }

    override fun onProductItemClicked(product: ProductItem) {
        findNavController().navigate(HomeFragmentDirections.actionHomeToDetailsFragment(product))
    }

    private fun createOfferInstance() {
        offerInstance = ProductItem(
            "1",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry.Lorem Ipsum has been the industry's standard dummy text.",
            "7",
            "https://firebasestorage.googleapis.com/v0/b/watchstore-9974c.appspot.com/o/watchImages%2Foffer.png?alt=media&token=9dd83b1c-d0d4-463a-ac18-ca5739fc3506",
            "Fossil Men's Golden Watch with Great Discount",
            "70",
            "200",
            6,
            false,

        )
    }

}