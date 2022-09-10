package com.example.ksbllc.data.storage

import com.example.ksbllc.data.storage.models.*

interface UserStorage {
    suspend fun registration(user: User): Boolean
    suspend fun signIn(authentificationParams: AuthentificationParams): Boolean
    suspend fun getAccessLVL(): String
    suspend fun getAllWarehouses(accessValue: String): ArrayList<Warehouse>
    suspend fun createNewWarehouse(storageName: String, products: ArrayList<Product>?): Boolean
    suspend fun renameWarehouse(obsoleteName: String, newName: String, capacity: Float, products: ArrayList<Product>?): Boolean
    suspend fun deleteWarehouse(nameWarehouse: String): Boolean
    suspend fun getAllUsersAccessLVL(): ArrayList<AccessLVLUnitData>
    suspend fun resetAccessLVL(accessLVLUnitData: AccessLVLUnitData): Boolean
    suspend fun addProduct(nameOFWarehouse: String, products: ArrayList<Product>): Boolean
    suspend fun changeAmountOfProduct(nameOFWarehouse: String, product: Product): Boolean
    suspend fun getAllProducts(nameOFWarehouse: String): ArrayList<Product>
    suspend fun sendPhotoAboutChange(photo: ByteArray): String
    suspend fun deleteUser(accessLVLUnitData: AccessLVLUnitData): Boolean
    suspend fun getAllWarehousesTitles(): ArrayList<String>
    suspend fun getAllDataChanges(warehouseName: String): ArrayList<Data>
    suspend fun getUserName(): String
    suspend fun getUserSurname(): String
    suspend fun sendAllDataChanges(data: Data): Boolean
}