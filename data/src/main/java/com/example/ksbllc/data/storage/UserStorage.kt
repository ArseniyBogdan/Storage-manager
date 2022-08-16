package com.example.ksbllc.data.storage

import com.example.ksbllc.data.storage.models.*
import com.ksbllc.domain.models.AccessLVLUnit

interface UserStorage {
    suspend fun registration(user: User): Boolean
    suspend fun signIn(authentificationParams: AuthentificationParams): Boolean
    suspend fun getAccessLVL(): String
    suspend fun getAllWarehouses(accessValue: String): ArrayList<Warehouse>
    suspend fun createNewWarehouse(storageName: String, capacity: Float, products: ArrayList<Product>?): Boolean
    suspend fun renameWarehouse(obsoleteName: String, newName: String, capacity: Float, products: ArrayList<Product>?): Boolean
    suspend fun deleteWarehouse(nameWarehouse: String): Boolean
    suspend fun getAllUsersAccessLVL(): ArrayList<AccessLVLUnitData>
    suspend fun resetAccessLVL(accessLVLUnitData: AccessLVLUnitData): Boolean

}