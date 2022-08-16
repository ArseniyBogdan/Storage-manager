package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Warehouse
import com.ksbllc.domain.repository.FirebaseRepository

class CreateNewWarehouseUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(warehouse: Warehouse): Boolean{
        val result: Boolean = firabaseRep.createNewWarehouse(warehouse)
        return result
    }
}