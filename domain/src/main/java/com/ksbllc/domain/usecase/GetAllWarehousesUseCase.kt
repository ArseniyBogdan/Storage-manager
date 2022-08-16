package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.Warehouse
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllWarehousesUseCase(private val firebaseRepository: FirebaseRepository) {

    suspend fun execute(accessLVL: String): ArrayList<Warehouse>{
        val result = firebaseRepository.getAllWarehouses(accessLVL)
        return result
    }

}