package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class SendPhotoAboutChangeUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(photo: Any) : Boolean{
        val result: Boolean = firebaseRep.sendPhotoAboutChange(photo)
        return result
    }
}