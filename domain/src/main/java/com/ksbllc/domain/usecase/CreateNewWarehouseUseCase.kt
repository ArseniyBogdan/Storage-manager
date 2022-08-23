package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Warehouse
import com.ksbllc.domain.repository.FirebaseRepository

class CreateNewWarehouseUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(warehouse: Warehouse): Boolean{
        val result: Boolean = firebaseRep.createNewWarehouse(warehouse)
        return result
    }
}