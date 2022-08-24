package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.Warehouse
import com.ksbllc.domain.repository.FirebaseRepository

class DeleteUserUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(accessLVLUnit: AccessLVLUnit): Boolean{
        val result: Boolean = firebaseRep.deleteUser(accessLVLUnit)
        return result
    }
}