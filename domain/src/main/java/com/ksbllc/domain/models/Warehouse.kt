package com.ksbllc.domain.models

import kotlin.collections.ArrayList

class Warehouse (name: String = "", capacity: Float = 0f, products: ArrayList<Product>? = ArrayList()) {
    val name: String = name
    val capacity: Float = capacity
    val products: ArrayList<Product>? = products
}