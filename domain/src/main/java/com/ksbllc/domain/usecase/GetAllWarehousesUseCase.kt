package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Warehouse
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllWarehousesUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(accessLVL: String): ArrayList<Warehouse>{
        val result = firebaseRep.getAllWarehouses(accessLVL)
        return result
    }

}