package com.example.ksbllc.presentation.viewModels

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.ksbllc.domain.usecase.RegistrationUseCase

class RegistrationActivityVM(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel(){

    val flagReg = MutableLiveData(false)
    val flagError = MutableLiveData("")

    suspend fun registration(firstName:String, secondName: String, email: String, password: String,
                             passwordConfirm: String){

        if(!isEmptyFields(firstName, secondName, email, password) &&
                password == passwordConfirm){
            val result = registrationUseCase.execute(firstName, secondName, email, password)

            // проверка ошибки регистрации
            if (result == true){
                flagReg.value = result
            }else{
                flagError.value = "Ошибка регистрации"
            }
        }
    }


    private fun isEmptyFields(name: String, surname: String, email: String, password: String): Boolean{
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
            flagError.value = "Вы не заполнили одно из полей"
            return true
        }
        if(!("@" in email)){
            flagError.value = "Вы неправильно указали email"
            return true
        }
        if(password.length < 8){
            flagError.value = "Пароль должен быть длиннее 7 символов"
            return true
        }
        return false
    }

    fun get_isEmptyFields(name: String, surname: String, email: String, password: String): Boolean{
        return isEmptyFields(name, surname, email, password)
    }

}