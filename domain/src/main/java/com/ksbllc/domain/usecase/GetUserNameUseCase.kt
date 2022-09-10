package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class GetUserNameUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(): String{
        val result: String = firebaseRep.getUserName()
        return result
    }
}