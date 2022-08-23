package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class AddProductUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(nameOfWarehouse: String, product: Product) : Boolean{
        val result: Boolean = firebaseRep.addProduct(nameOfWarehouse, product)
        return result
    }

}