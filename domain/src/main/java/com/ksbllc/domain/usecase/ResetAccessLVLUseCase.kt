package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class ResetAccessLVLUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(accessLVLUnit: AccessLVLUnit):Boolean{
        val result: Boolean = firebaseRep.resetAccessLVL(accessLVLUnit)
        return result
    }
}