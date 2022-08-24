package com.example.ksbllc.presentation.viewModels

import androidx.core.text.isDigitsOnly
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
import java.lang.NumberFormatException

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

    suspend fun changeAmountOfProduct(nameOfWarehouse: String, TypeOfProduct: String,
                                      changedValueNetto: Float, changedValueBrutto: Float): Boolean{
        val result = changeAmountOfProductUseCase.execute(nameOfWarehouse, TypeOfProduct,
            changedValueNetto, changedValueBrutto)
        return result
    }

    suspend fun addAmountOfProduct(nameOfWarehouse: String, product: ProductItemModel,
                                   changedValueNetto: Float, changedValueBrutto: Float){
        val currentValueNetto = changedValueNetto + product.amountOfProduct_Netto
        val currentValueBrutto = changedValueBrutto + product.amountOfProduct_Brutto

        changeAmountOfProduct(nameOfWarehouse, product.typeOfProduct, currentValueNetto, currentValueBrutto)
    }

    suspend fun withdrowAmountOfProduct(nameOfWarehouse: String, product: ProductItemModel,
                                   changedValueNetto: Float, changedValueBrutto: Float){
        val currentValueNetto = changedValueNetto - product.amountOfProduct_Netto
        val currentValueBrutto = changedValueBrutto - product.amountOfProduct_Brutto

        changeAmountOfProduct(nameOfWarehouse, product.typeOfProduct, currentValueNetto, currentValueBrutto)
    }

    suspend fun sendPhoto(photo: Any): Boolean{
        val result = sendPhotoAboutChangeUseCase.execute(photo)
        return result
    }

    fun checkCorrectness(netto: String, brutto: String): Boolean{
        if(netto.toFloatOrNull() == null || brutto.toFloatOrNull() == null){
            return false
        }
        return true
    }


}