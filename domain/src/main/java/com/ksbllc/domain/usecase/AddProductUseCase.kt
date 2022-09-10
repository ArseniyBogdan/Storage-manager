package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class AddProductUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(nameOfWarehouse: String, products: ArrayList<Product>) : Boolean{
        val result: Boolean = firebaseRep.addProduct(nameOfWarehouse, products)
        return result
    }

}