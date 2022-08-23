package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllUsersAccessLVLUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(): ArrayList<AccessLVLUnit> {
        val result: ArrayList<AccessLVLUnit> = firebaseRep.getAllUsersAccessLVL()
        return result
    }
}