package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class ChangeAmountofProductUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(nameOFWarehouse: String, nameOfProduct: String, changes: Float) : Boolean{
        val result: Boolean = firabaseRep.changeAmountOfProduct(nameOFWarehouse, nameOfProduct, changes)
        return result
    }
}