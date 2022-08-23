package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class GetAccessLVLUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute() : String{
        val result: String = firebaseRep.getAccessLVL()
        return result
    }
}