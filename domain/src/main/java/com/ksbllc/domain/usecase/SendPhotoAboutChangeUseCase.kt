package com.ksbllc.domain.usecase

import com.ksbllc.domain.repository.FirebaseRepository

class SendPhotoAboutChangeUseCase(private val firabaseRep: FirebaseRepository) {

    suspend fun execute(photo: Any) : Boolean{
        val result: Boolean = firabaseRep.sendPhotoAboutChange(photo)
        return result
    }
}