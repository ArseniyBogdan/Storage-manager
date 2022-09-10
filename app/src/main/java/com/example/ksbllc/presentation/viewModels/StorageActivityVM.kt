package com.example.ksbllc.presentation.viewModels

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.example.ksbllc.data.storage.models.Product
import com.example.ksbllc.presentation.models.ProductItemModel
import com.ksbllc.domain.models.DataChanges
import com.ksbllc.domain.usecase.*
import java.io.ByteArrayOutputStream
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class StorageActivityVM(
    private val addProductUseCase: AddProductUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val ChangeAmountOfProductUseCase: ChangeAmountOfProductUseCase,
    private val sendPhotoAboutChangeUseCase: SendPhotoAboutChangeUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getUserSurnameUseCase: GetUserSurnameUseCase,
    private val sendAllDataChangesUseCase: SendAllDataChangesUseCase,
): ViewModel() {
    val flagAdd = MutableLiveData(false)
    val flagSetPhoto = MutableLiveData(false)
    private lateinit var photo: Bitmap

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

    suspend fun addProduct(nameOfWarehouse: String, products: ArrayList<ProductItemModel>): Boolean{
        val productsForSend = ArrayList<com.ksbllc.domain.models.Product>()

        for(product in products){
            productsForSend.add(com.ksbllc.domain.models.Product(product.typeOfProduct,
                product.amountOfProduct_Netto, product.amountOfProduct_Brutto))
        }

        val result = addProductUseCase.execute(nameOfWarehouse, productsForSend)
        flagAdd.value = true

        return result
    }

    suspend fun changeAmountOfProduct(nameOfWarehouse: String, TypeOfProduct: String,
                                      changedValueNetto: Float, changedValueBrutto: Float): Boolean{
        val result = ChangeAmountOfProductUseCase.execute(nameOfWarehouse, TypeOfProduct,
            changedValueNetto, changedValueBrutto)
        return result
    }

    suspend fun addAmountOfProduct(nameOfWarehouse: String, product: ProductItemModel,
                                   changedValueNetto: Float, changedValueBrutto: Float){
        val currentValueNetto = changedValueNetto + product.amountOfProduct_Netto
        val currentValueBrutto = changedValueBrutto + product.amountOfProduct_Brutto

        changeAmountOfProduct(nameOfWarehouse, product.typeOfProduct, currentValueNetto, currentValueBrutto)
        sendAllDataChanges(nameOfWarehouse = nameOfWarehouse, valueChanges = changedValueNetto,
            typeOfChanges = "+", typeOfProduct = product.typeOfProduct)
    }

    suspend fun withdrowAmountOfProduct(nameOfWarehouse: String, product: ProductItemModel,
                                   changedValueNetto: Float, changedValueBrutto: Float){
        val currentValueNetto = changedValueNetto - product.amountOfProduct_Netto
        val currentValueBrutto = changedValueBrutto - product.amountOfProduct_Brutto

        changeAmountOfProduct(nameOfWarehouse, product.typeOfProduct, currentValueNetto, currentValueBrutto)
        sendAllDataChanges(nameOfWarehouse = nameOfWarehouse, valueChanges = changedValueNetto,
            typeOfChanges = "-", typeOfProduct = product.typeOfProduct)
    }

    suspend fun sendPhoto(): String{
        val baos = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray: ByteArray = baos.toByteArray()
        val result = sendPhotoAboutChangeUseCase.execute(byteArray)
        return result
    }

    suspend fun sendAllDataChanges(nameOfWarehouse: String, valueChanges: Float,
                                   typeOfChanges: String, typeOfProduct: String): Boolean{
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val dataChanges = DataChanges(
            date = currentDate,
            name = getUserNameUseCase.execute(),
            surname = getUserSurnameUseCase.execute(),
            nameOfWarehouse = nameOfWarehouse,
            photoPath = sendPhoto(),
            valueChanges = valueChanges,
            typeOfChanges = typeOfChanges,
            typeOfProduct = typeOfProduct
        )

        val result = sendAllDataChangesUseCase.execute(dataChanges = dataChanges)

        return result
    }

    fun checkCorrectness(netto: String, brutto: String): Boolean{
        if(netto.toFloatOrNull() == null || brutto.toFloatOrNull() == null){
            return false
        }
        if(netto.toFloat() > brutto.toFloat()){
            return false
        }
        return true
    }

    fun setPhoto(newPhoto: Bitmap){
        photo = newPhoto
        flagSetPhoto.value = true
    }

}