package com.example.ksbllc.presentation.viewModels

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.ksbllc.domain.usecase.AuthentificationUseCase
import com.ksbllc.domain.usecase.GetAllUsersAccessLVLUseCase
import com.ksbllc.domain.usecase.ResetAccessLVLUseCase

class AuthentificationActivityVM(
    private val authentificationUseCase: AuthentificationUseCase
): ViewModel() {
    val flagSI = MutableLiveData(false)
    val flagError = MutableLiveData("")

    suspend fun signIn(email: String, password: String){
        if(!isEmptyFields(email, password)){
            val result = authentificationUseCase.execute(email = email, password = password)
            if(result == true){
                flagSI.value = true
            }
            else{
                flagError.value = "Ошибка входа"
            }
        }
    }

    private fun isEmptyFields(email: String, password: String): Boolean {
        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
            flagError.value = "Вы не заполнили одно из полей"
            return true
        }
        if(!("@" in email)){
            flagError.value = "Вы неправильно указали email"
            return true
        }
        return false
    }
}