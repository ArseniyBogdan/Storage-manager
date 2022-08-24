package com.ksbllc.domain.repository

import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.Product
import com.ksbllc.domain.models.Warehouse

interface FirebaseRepository {
    suspend fun registration(firstName:String, secondName: String, email: String,
                     password: String): Boolean
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun getAccessLVL(): String
    suspend fun getAllWarehouses(accessValue: String): ArrayList<Warehouse>
    suspend fun createNewWarehouse(warehouse: Warehouse): Boolean
    suspend fun renameWarehouse(obsoleteName: String, newName: String,
                                capacity: Float, products: ArrayList<Product>?): Boolean
    suspend fun deleteWarehouse(nameWarehouse: String): Boolean
    suspend fun getAllUsersAccessLVL(): ArrayList<AccessLVLUnit>
    suspend fun resetAccessLVL(accessLVLUnit: AccessLVLUnit): Boolean
    suspend fun addProduct(nameOFWarehouse: String, product: Product): Boolean
    suspend fun changeAmountOfProduct(nameOFWarehouse: String, product: Product): Boolean
    suspend fun getAllProducts(nameOFWarehouse: String): ArrayList<Product>
    suspend fun sendPhotoAboutChange(photo: Any): Boolean

}