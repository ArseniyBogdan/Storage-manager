package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class GetAccessLVLUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute() : String{
        val result: String = firabaseRep.getAccessLVL()
        return result
    }
}