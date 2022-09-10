//package com.example.ksbllc.presentation.viewModels
//
//import androidx.lifecycle.ViewModel
//import com.example.ksbllc.presentation.models.DataModel
//import com.ksbllc.domain.models.DataChanges
//import com.ksbllc.domain.usecase.GetAllDataChangesUseCase
//import com.ksbllc.domain.usecase.GetPhotoByPathUseCase
//
//class DataListActivityVM(
//    private val getAllDataChangesUseCase: GetAllDataChangesUseCase,
//    private val getPhotoByPathUseCase: GetPhotoByPathUseCase
//): ViewModel() {
//
//    suspend fun getAllDataChanges(nameOfWarehouse: String): ArrayList<DataModel>{
//        val result = ArrayList<DataModel>()
//        val dataChangeList: ArrayList<DataChanges> = getAllDataChangesUseCase.execute(nameOfWarehouse)
//        for(dataChange in dataChangeList){
//            result.add(
//                DataModel(
//                date = dataChange.date,
//                name = dataChange.name,
//                surname = dataChange.surname,
//                photoPath = dataChange.photoPath,
//                valueChanges = dataChange.valueChanges,
//                typeOfProduct = dataChange.typeOfProduct)
//            )
//        }
//        return result
//    }
//
//    suspend fun getPhotoByPath(path: String): Any{
//        TODO("Сделать получение фотки")
//        val photo = getPhotoByPathUseCase.execute()
//        return photo
//    }
//}