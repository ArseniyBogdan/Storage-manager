package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class SendPhotoAboutChangeUseCase(private val firebaseRep: FirebaseRepository) {

    suspend fun execute(photo: ByteArray) : String{
        val result: String = firebaseRep.sendPhotoAboutChange(photo)
        return result
    }
}