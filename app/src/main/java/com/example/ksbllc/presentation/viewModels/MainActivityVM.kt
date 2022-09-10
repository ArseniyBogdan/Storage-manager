package com.example.ksbllc.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ksbllc.data.repository.FirebaseRepositoryImp
import com.example.ksbllc.data.storage.firebase.FirebaseUserStorage
import com.example.ksbllc.presentation.models.ProductItemModel
import com.ksbllc.domain.models.Product
import com.ksbllc.domain.models.Warehouse
import com.ksbllc.domain.usecase.*

class MainActivityVM(
    private val getAccessLVLUseCase: GetAccessLVLUseCase,
    private val getAllWarehousesUseCase: GetAllWarehousesUseCase,
    private val createNewWarehouseUseCase: CreateNewWarehouseUseCase,
    private val renameWarehouseUseCase: RenameWarehouseUseCase,
    private val deleteWarehouseUseCase: DeleteWarehouseUseCase
) : ViewModel() {
    val flagAdd = MutableLiveData(false)
    val flagDelete = MutableLiveData(false)
    val flagRename = MutableLiveData(false)
    val flagRestart = MutableLiveData(false)


    suspend fun getAccessLVL(): String{
        val result = getAccessLVLUseCase.execute()
        return result
    }

    suspend fun getAllWarehouses(): ArrayList<Warehouse>{
        val result = getAllWarehousesUseCase.execute(accessLVL = getAccessLVL())
        return result
    }

    suspend fun getAllWarehouses(accessLVL: String): ArrayList<Warehouse>{
        val result = getAllWarehousesUseCase.execute(accessLVL = accessLVL)
        return result
    }

    fun createListOfProducts(warehouse: Warehouse):ArrayList<ProductItemModel>{
        val items: ArrayList<ProductItemModel> = ArrayList()

        for(product in warehouse.products!!){
            items.add(ProductItemModel(product.name, product.amountOfProduct_Netto, product.amountOfProduct_Brutto))
        }
        return items
    }

    suspend fun createNewWarehouse(storageName: String, capacity: Float = 10000f, products: ArrayList<Product>?) {
        val warehouse = Warehouse(storageName, capacity, products)
        val result = createNewWarehouseUseCase.execute(warehouse)
        flagAdd.value = result
    }

    suspend fun renameWarehouse(obsoleteName: String, newName: String, capacity: Float, products: ArrayList<Product>?){
        val result = renameWarehouseUseCase.execute(obsoleteName, newName, capacity, products)
        flagRename.value = result
    }
    
    suspend fun deleteWarehouse(warehouseName: String){
        val result = deleteWarehouseUseCase.execute(warehouseName)
        flagDelete.value = result
    }

    fun replaceWithNewName(obsoleteName: String, newName: String,
                           warehouses: ArrayList<Warehouse>): ArrayList<Warehouse>{
        val list = ArrayList<Warehouse>()
        for(warehouse in warehouses){
            if(warehouse.name == obsoleteName){
                list.add(Warehouse(newName, warehouse.capacity, warehouse.products))
            }
            else{
                list.add(warehouse)
            }
        }
        return list
    }

    fun deleteWithName(nameOFStorage: String, warehouses: ArrayList<Warehouse>): ArrayList<Warehouse>{
        val list = ArrayList<Warehouse>()
        for(warehouse in warehouses){
            if(warehouse.name != nameOFStorage){
                list.add(warehouse)
            }
        }
        return list
    }

}