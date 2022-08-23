package com.example.ksbllc.di

import com.ksbllc.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {

    factory<AddProductUseCase>{
        AddProductUseCase(firebaseRep = get())
    }

    factory<AuthentificationUseCase>{
        AuthentificationUseCase(firebaseRep = get())
    }

    factory<changeAmountOfProductUseCase>{
        changeAmountOfProductUseCase(firebaseRep = get())
    }

    factory<CreateNewWarehouseUseCase>{
        CreateNewWarehouseUseCase(firebaseRep = get())
    }

    factory<DeleteWarehouseUseCase>{
        DeleteWarehouseUseCase(firebaseRep = get())
    }

    factory<GetAccessLVLUseCase>{
        GetAccessLVLUseCase(firebaseRep = get())
    }

    factory<GetAllProductsUseCase>{
        GetAllProductsUseCase(firebaseRep = get())
    }

    factory<GetAllUsersAccessLVLUseCase>{
        GetAllUsersAccessLVLUseCase(firebaseRep = get())
    }

    factory<GetAllWarehousesUseCase>{
        GetAllWarehousesUseCase(firebaseRep = get())
    }

    factory<RegistrationUseCase>{
        RegistrationUseCase(firebaseRep = get())
    }

    factory<RenameWarehouseUseCase>{
        RenameWarehouseUseCase(firebaseRep = get())
    }

    factory<ResetAccessLVLUseCase>{
        ResetAccessLVLUseCase(firebaseRep = get())
    }

    factory<SendPhotoAboutChangeUseCase>{
        SendPhotoAboutChangeUseCase(firebaseRep = get())
    }

}