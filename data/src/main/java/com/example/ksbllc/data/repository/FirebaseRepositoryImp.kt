package com.example.ksbllc.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.ksbllc.data.storage.models.User
import com.example.ksbllc.data.storage.models.AuthentificationParams
import com.example.ksbllc.data.storage.UserStorage
import com.example.ksbllc.data.storage.models.AccessLVLUnitData
import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.Product
import com.ksbllc.domain.repository.FirebaseRepository

class FirebaseRepositoryImp(private val userStorage: UserStorage) : FirebaseRepository {

    override suspend fun registration(firstName:String, secondName: String, email: String,
                                      password: String): Boolean{
        val user = User(firstName, secondName, email, password, accessLVL = "all")
        val result = userStorage.registration(user)
        return result
    }

    override suspend fun signIn(email: String, password: String): Boolean{
        val authentificationParams = AuthentificationParams(email, password)
        val result = userStorage.signIn(authentificationParams)
        return result
    }

    override suspend fun getAccessLVL(): String {
        val result = userStorage.getAccessLVL()
        return result
    }

    override suspend fun getAllWarehouses(accessValue: String): ArrayList<com.ksbllc.domain.models.Warehouse> {
        val result = userStorage.getAllWarehouses(accessValue = accessValue)
        var capacity: Float = 0f
        val warehouses = ArrayList<com.ksbllc.domain.models.Warehouse>()

        for(warehouse in result){
            val products = ArrayList<Product>()
            for(product in warehouse.products!!){
                capacity += product.amountOfProduct_Netto
                products.add(Product(name = product.name,
                    amountOfProduct_Netto = product.amountOfProduct_Netto,
                    amountOfProduct_Brutto = product.amountOfProduct_Brutto))
            }
            warehouses.add(com.ksbllc.domain.models.Warehouse(warehouse.name, capacity, products))
            capacity = 0f
        }

        return warehouses
    }

    override suspend fun createNewWarehouse(warehouse: com.ksbllc.domain.models.Warehouse): Boolean {
        val listOfProducts = ArrayList<com.example.ksbllc.data.storage.models.Product>()

        for(product in warehouse.products!!){
            listOfProducts.add(
                com.example.ksbllc.data.storage.models.Product(
                    name = product.name,
                    amountOfProduct_Netto = product.amountOfProduct_Netto,
                    amountOfProduct_Brutto = product.amountOfProduct_Brutto
                )
            )
        }

        val result = userStorage.createNewWarehouse(warehouse.name, listOfProducts)
        return result
    }

    override suspend fun renameWarehouse(obsoleteName: String, newName: String,
                                         capacity: Float, products: ArrayList<Product>?): Boolean {
        val listOfProducts = ArrayList<com.example.ksbllc.data.storage.models.Product>()
        if (products != null) {
            for(product in products){
                listOfProducts.add(
                    com.example.ksbllc.data.storage.models.Product(
                        name = product.name,
                        amountOfProduct_Netto = product.amountOfProduct_Netto,
                        amountOfProduct_Brutto = product.amountOfProduct_Brutto                    )
                )
            }
        }

        val result = userStorage.renameWarehouse(obsoleteName, newName, capacity, listOfProducts)
        return result
    }

    override suspend fun deleteWarehouse(nameWarehouse: String): Boolean {
        val result = userStorage.deleteWarehouse(nameWarehouse)
        return result
    }

    override suspend fun getAllUsersAccessLVL(): ArrayList<AccessLVLUnit>{

        val pre_result: ArrayList<AccessLVLUnitData> = userStorage.getAllUsersAccessLVL()
        val result = ArrayList<AccessLVLUnit>()

        for(AccessLVLUnit in pre_result){
            result.add(AccessLVLUnit(AccessLVLUnit.name, AccessLVLUnit.surname, AccessLVLUnit.accessLVL))
        }

        return result
    }

    override suspend fun resetAccessLVL(accessLVLUnit: AccessLVLUnit): Boolean {
        val accessLVLUnitData = AccessLVLUnitData(name = accessLVLUnit.name,
            surname = accessLVLUnit.surname, accessLVL = accessLVLUnit.accessLVL)

        val result = userStorage.resetAccessLVL(accessLVLUnitData)
        return result
    }

    override suspend fun addProduct(nameOFWarehouse: String, product: Product): Boolean {
        val result = userStorage.addProduct(nameOFWarehouse,
            com.example.ksbllc.data.storage.models.Product(product.name,
                product.amountOfProduct_Netto, product.amountOfProduct_Brutto))
        return result
    }

    override suspend fun getAllProducts(nameOFWarehouse: String): ArrayList<Product> {
        val pre_result: ArrayList<com.example.ksbllc.data.storage.models.Product> = userStorage.getAllProducts(nameOFWarehouse)
        val result = ArrayList<Product>()

        for(product in pre_result){
            result.add(Product(product.name, product.amountOfProduct_Netto, product.amountOfProduct_Brutto))
        }

        return result
    }

    override suspend fun changeAmountOfProduct(
        nameOFWarehouse: String,
        product: Product,
    ): Boolean {
        val productForData = com.example.ksbllc.data.storage.models.Product(product.name,
            product.amountOfProduct_Netto, product.amountOfProduct_Brutto)
        val result = userStorage.changeAmountOfProduct(nameOFWarehouse, productForData)
        return result
    }

    override suspend fun deleteUser(accessLVLUnit: AccessLVLUnit): Boolean {

        val accessLVLUnitData = AccessLVLUnitData(accessLVLUnit.name,
            accessLVLUnit.surname, accessLVLUnit.accessLVL)

        val result = userStorage.deleteUser(accessLVLUnitData)
        return result
    }

    override suspend fun sendPhotoAboutChange(photo: Any): Boolean {
        TODO("реализовать нормальную передачу фото")
        val result = userStorage.sendPhotoAboutChange(photo)
        return result
    }


}