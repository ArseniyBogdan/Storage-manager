package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class RenameWarehouseUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(obsoleteName:String, newName: String,
                        capacity: Float, products: ArrayList<Product>?):Boolean{
        val result: Boolean = firebaseRep.renameWarehouse(obsoleteName, newName, capacity, products)
        return result
    }
}