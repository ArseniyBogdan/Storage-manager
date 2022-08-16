package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class RenameWarehouseUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(obsoleteName:String, newName: String,
                        capacity: Float, products: ArrayList<Product>?):Boolean{
        val result: Boolean = firabaseRep.renameWarehouse(obsoleteName, newName, capacity, products)
        return result
    }
}