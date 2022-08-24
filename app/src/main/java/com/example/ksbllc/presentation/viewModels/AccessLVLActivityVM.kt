package com.example.ksbllc.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.User
import com.ksbllc.domain.usecase.DeleteUserUseCase
import com.ksbllc.domain.usecase.GetAllUsersAccessLVLUseCase
import com.ksbllc.domain.usecase.ResetAccessLVLUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class AccessLVLActivityVM(
    private val getAllUsersAccessLVLUseCase: GetAllUsersAccessLVLUseCase,
    private val resetAccessLVLUseCase: ResetAccessLVLUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
): ViewModel() {
    val flagReset = MutableLiveData(false)
    val flagDelete = MutableLiveData(false)
    val flagVisible = MutableLiveData(false)

    suspend fun getAllWorkersAccessLVL(): ArrayList<AccessLVLUnit>{
        val result = getAllUsersAccessLVLUseCase.execute()
        return result
    }

    suspend fun resetAccessLVL(accessLVLUnit :AccessLVLUnit): Boolean{
        val result = resetAccessLVLUseCase.execute(accessLVLUnit)
        flagReset.value = result
        return result
    }

    suspend fun deleteUsers(accessLVLUnits: ArrayList<AccessLVLUnit>): Boolean{
        val results = ArrayList<Boolean>()
        viewModelScope.launch(Dispatchers.IO){
            for(accessLVLUnit in accessLVLUnits){
                results.add(deleteUserUseCase.execute(accessLVLUnit))
            }
        }
        for(result in results){
            if(!result){
               return false
            }
        }
        flagDelete.value = true
        return true
    }
}