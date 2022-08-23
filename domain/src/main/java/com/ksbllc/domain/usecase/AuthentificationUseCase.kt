package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class AuthentificationUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(email: String, password: String) : Boolean{
        val result: Boolean = firebaseRep.signIn(email, password)
        return result
    }
}