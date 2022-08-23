package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class DeleteWarehouseUseCase(private val firebaseRep: FirebaseRepository) {
    suspend fun execute(warehouseName: String): Boolean{
        val result = firebaseRep.deleteWarehouse(warehouseName)
        return result
    }
}