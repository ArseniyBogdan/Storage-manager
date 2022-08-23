package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllProductsUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(nameOfWarehouse: String) : ArrayList<Product>{
        val result: ArrayList<Product> = firebaseRep.getAllProducts(nameOfWarehouse)
        return result
    }
}