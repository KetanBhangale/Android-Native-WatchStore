package com.example.watchstoreapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarttItem(
    val cat_id: String?="",
    val id: String?="",
    val img: String?="",
    val name: String?="",
    val offer: String?="",
    val price: String?="",
    val quantity: Int?=0
):Parcelable