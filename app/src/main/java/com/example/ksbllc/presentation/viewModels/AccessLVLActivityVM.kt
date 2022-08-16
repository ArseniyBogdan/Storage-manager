package com.example.ksbllc.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.User
import com.ksbllc.domain.usecase.GetAllUsersAccessLVLUseCase
import com.ksbllc.domain.usecase.ResetAccessLVLUseCase

class AccessLVLActivityVM: ViewModel() {
    private val fireBaseRep by lazy(LazyThreadSafetyMode.NONE) {
        FirebaseRepositoryImp(FirebaseUserStorage())
    }

    private val getAllUsersAccessLVLUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetAllUsersAccessLVLUseCase(fireBaseRep)
    }

    private val resetAccessLVLUseCase by lazy(LazyThreadSafetyMode.NONE) {
        ResetAccessLVLUseCase(fireBaseRep)
    }

    val flagReset = MutableLiveData(false)

    suspend fun getAllWorkersAccessLVL(): ArrayList<AccessLVLUnit>{
        val result = getAllUsersAccessLVLUseCase.execute()
        return result
    }

    suspend fun resetAccessLVL(accesLVLUnit :AccessLVLUnit): Boolean{
        val result = resetAccessLVLUseCase.execute(accesLVLUnit)
        flagReset.value = true
        return result
    }

    suspend fun deleteUser(): Boolean{
        TODO("Сделать удаление пользователя из БД")
    }

}