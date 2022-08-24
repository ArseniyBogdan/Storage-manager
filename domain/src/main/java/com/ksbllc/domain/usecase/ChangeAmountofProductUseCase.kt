package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class changeAmountOfProductUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(nameOFWarehouse: String, nameOfProduct: String, changeNetto: Float, changeBrutto: Float) : Boolean{
        val product = Product(nameOfProduct, changeNetto, changeBrutto)
        val result: Boolean = firebaseRep.changeAmountOfProduct(nameOFWarehouse, product)
        return result
    }
}