package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.DataChanges
import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllDataChangesUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(nameOfWarehouse: String) : ArrayList<DataChanges>{
        val result: ArrayList<DataChanges> = firebaseRep.getAllDataChanges(nameOfWarehouse)
        return result
    }
}