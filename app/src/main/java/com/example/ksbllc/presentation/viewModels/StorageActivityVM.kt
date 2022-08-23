package com.example.ksbllc.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.example.ksbllc.data.storage.models.Product
import com.example.ksbllc.presentation.models.ProductItemModel
import com.ksbllc.domain.usecase.AddProductUseCase
import com.ksbllc.domain.usecase.changeAmountOfProductUseCase
import com.ksbllc.domain.usecase.GetAllProductsUseCase
import com.ksbllc.domain.usecase.SendPhotoAboutChangeUseCase

class StorageActivityVM(
    private val addProductUseCase: AddProductUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val changeAmountOfProductUseCase: changeAmountOfProductUseCase,
    private val sendPhotoAboutChangeUseCase: SendPhotoAboutChangeUseCase
): ViewModel() {
    val flagAdd = MutableLiveData(false)

    suspend fun getAllProducts(nameOfWarehouse: String): ArrayList<ProductItemModel>{
        val pre_result = getAllProductsUseCase.execute(nameOfWarehouse = nameOfWarehouse)
        val result = ArrayList<ProductItemModel>()

        for(product in pre_result){
            result.add(
                ProductItemModel(typeOfProduct = product.name,
                amountOfProduct_Netto = product.amountOfProduct_Netto,
                amountOfProduct_Brutto = product.amountOfProduct_Brutto)
            )
        }

        return result
    }

    suspend fun addProduct(nameOfWarehouse: String, product: ProductItemModel): Boolean{
        val result = addProductUseCase.execute(nameOfWarehouse,
            com.ksbllc.domain.models.Product(product.typeOfProduct, product.amountOfProduct_Netto, product.amountOfProduct_Brutto))

        return result
    }

    suspend fun changeAmountOfProduct(nameOfWarehouse: String, TypeOfProduct: String, changedValue: Float): Boolean{
        val result = changeAmountOfProductUseCase.execute(nameOfWarehouse, TypeOfProduct, changedValue)
        return result
    }

    suspend fun sendPhoto(photo: Any): Boolean{
        val result = sendPhotoAboutChangeUseCase.execute(photo)
        return result
    }

}