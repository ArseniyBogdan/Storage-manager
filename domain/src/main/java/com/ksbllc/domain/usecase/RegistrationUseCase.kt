package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class RegistrationUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(firstName: String, secondName: String, email: String, password: String) : Boolean{
        val result: Boolean = firabaseRep.registration(firstName,
            secondName, email, password)
        return result
    }
}