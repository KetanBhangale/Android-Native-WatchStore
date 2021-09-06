package com.example.watchstoreapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchstoreapp.model.CarttItem
import com.example.watchstoreapp.model.CategoryItem
import com.example.watchstoreapp.model.ProductItem
import com.example.watchstoreapp.model.User
import com.example.watchstoreapp.repository.StoreRepository
import com.example.watchstoreapp.utils.Constant
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(private val repository: StoreRepository,
private val db: FirebaseFirestore
):ViewModel() {

    private var _catList = MutableLiveData<ArrayList<CategoryItem>>()
    val catList: LiveData<ArrayList<CategoryItem>> get() = _catList

    private var _productList = MutableLiveData<ArrayList<ProductItem>>()
    val productList: LiveData<ArrayList<ProductItem>> get() = _productList

    private var _cartList = MutableLiveData<ArrayList<CarttItem>>()
    val cartList: LiveData<ArrayList<CarttItem>> get() = _cartList

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
       // getAllCategories()
    }

    fun getAllCategories(){
        viewModelScope.launch {
            _catList.postValue(repository.getAllCategories())
        }

    }

    fun getProductByCategory(catId:String){
        viewModelScope.launch {
//            var data:ArrayList<ProductItem>?=null
//            if(catId=="All"){
//                data = repository.getProductByCategory("All")
//            }else{
//                data = repository.getProductByCategory(catId)
//            }
            var data = repository.getProductByCategory(catId)
            delay(500)
            _productList.postValue(data!!)
        }
    }

    fun updateFav(product:ProductItem){
        viewModelScope.launch {
            repository.updateFavorite(product)
        }

    }
    fun getCartItems(){
        viewModelScope.launch {
            _cartList.postValue(repository.getCartItems())
        }

    }

    fun addToCart(product:ProductItem){
        viewModelScope.launch {
            repository.addToCart(product)
        }
    }

//    fun deleteAllCartItems(list: ArrayList<CarttItem>){
//        viewModelScope.launch {
//            repository.deleteAllCardItems(list)
//        }
//    }
    suspend fun deleteAllCartItems(list: ArrayList<CarttItem>){
            repository.deleteAllCardItems(list)
        }
    fun deleteCartItem(productId:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCartItem(productId)

        }

    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)

        }
    }


    fun getUserDetails(email: String, password:String) {

            Log.i("email",email)
            Log.i("password",password)
            val collection = db.collection(Constant.USER_TABEL)
            collection.whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { documents ->

                    if (documents.size()==0){
                        //Log.d("No User", documents.size().toString())
                        _user.postValue(User("1","1","1","1","1"))
                        //Log.d("No User Dummy Data", documents.size().toString())
                    }
                    for (document in documents) {
                        var data = document.toObject(User::class.java)
                        _user.postValue(data)
                        //Log.d("valid User", data.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Error", "Error getting documents: ", exception)
                }




    }
}