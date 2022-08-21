package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class AddProductUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(nameOfWarehouse: String, product: Product) : Boolean{
        val result: Boolean = firabaseRep.addProduct(nameOfWarehouse, product)
        return result
    }

}