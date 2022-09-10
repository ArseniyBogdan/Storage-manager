package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.DataChanges
import com.ksbllc.domain.repository.FirebaseRepository

class SendAllDataChangesUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(dataChanges: DataChanges): Boolean{
        val result: Boolean = firebaseRep.sendAllDataChanges(dataChanges)
        return result
    }
}