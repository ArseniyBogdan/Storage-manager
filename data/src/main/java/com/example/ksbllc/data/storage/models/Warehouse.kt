package com.example.ksbllc.data.storage.models

import com.example.ksbllc.data.storage.models.Product
import kotlin.collections.ArrayList

class Warehouse (name: String = "", products: ArrayList<Product>? = ArrayList()) {
    val name: String = name
    val products: ArrayList<Product>? = products
}