package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class DeleteWarehouseUseCase(private val firabaseRep: FirebaseRepository) {
    suspend fun execute(warehouseName: String): Boolean{
        val result = firabaseRep.deleteWarehouse(warehouseName)
        return result
    }
}