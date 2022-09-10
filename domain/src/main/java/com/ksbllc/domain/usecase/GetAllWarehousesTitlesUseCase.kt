package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllWarehousesTitlesUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(): ArrayList<String> {
        val result: ArrayList<String> = firebaseRep.getAllWarehousesTitles()
        return result
    }
}