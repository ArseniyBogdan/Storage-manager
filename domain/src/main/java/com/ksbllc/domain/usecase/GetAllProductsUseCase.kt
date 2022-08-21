package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllProductsUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(nameOfWarehouse: String) : ArrayList<Product>{
        val result: ArrayList<Product> = firabaseRep.getAllProducts(nameOfWarehouse)
        return result
    }
}