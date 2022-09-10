package com.example.ksbllc.di

import com.example.ksbllc.presentation.viewModels.*
import com.ksbllc.domain.usecase.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<MainActivityVM>(){
        MainActivityVM(
            getAccessLVLUseCase = get(),
            getAllWarehousesUseCase = get(),
            createNewWarehouseUseCase = get(),
            renameWarehouseUseCase = get(),
            deleteWarehouseUseCase = get(),
        )
    }

    viewModel<AuthentificationActivityVM>(){
        AuthentificationActivityVM(
            authentificationUseCase = get()
        )
    }

    viewModel<AccessLVLActivityVM>(){
        AccessLVLActivityVM(
            getAllUsersAccessLVLUseCase = get(),
            resetAccessLVLUseCase = get(),
            deleteUserUseCase = get(),
            getAllWarehousesTitlesUseCase = get()
        )
    }

    viewModel<RegistrationActivityVM>(){
        RegistrationActivityVM(
            registrationUseCase = get()
        )
    }

    viewModel<StorageActivityVM>(){
        StorageActivityVM(
            addProductUseCase = get(),
            getAllProductsUseCase = get(),
            ChangeAmountOfProductUseCase = get(),
            sendPhotoAboutChangeUseCase = get(),
            getUserNameUseCase = get(),
            getUserSurnameUseCase = get(),
            sendAllDataChangesUseCase = get()
        )
    }

//    viewModel<DataListActivityVM>(){
//        DataListActivityVM(
//            getAllDataChangesUseCase = get(),
//            getPhotoByPathUseCase = get()
//        )
//    }
}