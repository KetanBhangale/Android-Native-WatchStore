package com.example.watchstoreapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductItem(
    val cat_id: String?="",
    val description: String?="",
    val id: String?="",
    val img: String?="",
    val name: String?="",
    val offer: String?="",
    val price: String?="",
    var quantity: Int?=0,
    var isFavorite: Boolean?= false
):Parcelable