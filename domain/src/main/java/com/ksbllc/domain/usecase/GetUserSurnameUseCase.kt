package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class GetUserSurnameUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute() : String{
        val result: String = firebaseRep.getUserSurname()
        return result
    }
}