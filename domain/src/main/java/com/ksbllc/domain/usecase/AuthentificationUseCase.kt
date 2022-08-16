package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class AuthentificationUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(email: String, password: String) : Boolean{
        val result: Boolean = firabaseRep.signIn(email, password)
        return result
    }
}