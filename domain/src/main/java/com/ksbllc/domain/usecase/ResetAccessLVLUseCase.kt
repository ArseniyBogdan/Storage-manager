package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class ResetAccessLVLUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(accessLVLUnit: AccessLVLUnit):Boolean{
        val result: Boolean = firabaseRep.resetAccessLVL(accessLVLUnit)
        return result
    }
}