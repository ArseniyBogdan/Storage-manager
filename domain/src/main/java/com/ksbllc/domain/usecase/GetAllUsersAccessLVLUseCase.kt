package com.ksbllc.domain.usecase

import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.repository.FirebaseRepository

class GetAllUsersAccessLVLUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(): ArrayList<AccessLVLUnit> {
        val result: ArrayList<AccessLVLUnit> = firabaseRep.getAllUsersAccessLVL()
        return result
    }
}