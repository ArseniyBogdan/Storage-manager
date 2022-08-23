package com.example.ksbllc.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.User
import com.ksbllc.domain.usecase.GetAllUsersAccessLVLUseCase
import com.ksbllc.domain.usecase.ResetAccessLVLUseCase

class AccessLVLActivityVM(
    private val getAllUsersAccessLVLUseCase: GetAllUsersAccessLVLUseCase,
    private val resetAccessLVLUseCase: ResetAccessLVLUseCase
): ViewModel() {
    val flagReset = MutableLiveData(false)

    suspend fun getAllWorkersAccessLVL(): ArrayList<AccessLVLUnit>{
        val result = getAllUsersAccessLVLUseCase.execute()
        return result
    }

    suspend fun resetAccessLVL(accesLVLUnit :AccessLVLUnit): Boolean{
        val result = resetAccessLVLUseCase.execute(accesLVLUnit)
        flagReset.value = result
        return result
    }

    suspend fun deleteUser(): Boolean{
        TODO("Сделать удаление пользователя из БД")
    }

}