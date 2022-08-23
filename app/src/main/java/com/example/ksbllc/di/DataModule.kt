package com.example.ksbllc.di

import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.UserStorage
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.ksbllc.domain.repository.FirebaseRepository
import org.koin.dsl.module

val dataModule = module {

    single<UserStorage> {
        FirebaseUserStorage()
    }

    single<FirebaseRepository> {
        FirebaseRepositoryImp(userStorage = get())
    }

}