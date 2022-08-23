package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class changeAmountOfProductUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(nameOFWarehouse: String, nameOfProduct: String, changes: Float) : Boolean{
        val result: Boolean = firebaseRep.changeAmountOfProduct(nameOFWarehouse, nameOfProduct, changes)
        return result
    }
}