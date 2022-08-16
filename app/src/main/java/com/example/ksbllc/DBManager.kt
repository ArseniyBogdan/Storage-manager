//package com.example.ksbllc
//
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.util.Log
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import com.example.ksbllc.RegAndAuth.AuthentificationActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//class DBManager {
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val db: FirebaseDatabase = FirebaseDatabase.getInstance("https://ksb-llc-default-rtdb.europe-west1.firebasedatabase.app/")
//    private val users: DatabaseReference = db.getReference("Users")
//    private val warehouses: DatabaseReference = db.getReference("Warehouses")
//
//
//    fun getNameAndSurname() : ArrayList<String>{
//        val list: ArrayList<String> = ArrayList()
//        val userEmail = auth.currentUser?.email
//
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val user = snapshot.getValue(User::class.java)
//
//                if (user != null) {
//                    if (user.email == userEmail) {
//                        list.add(user.name)
//                        list.add(user.surname)
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("My_Log", "LoadPost:onCancelled", error.toException())
//            }
//        }
//
//        users.addListenerForSingleValueEvent(postListener)
//
//        return list
//    }
//
//    fun renameWarehouse(obsoleteName:String, newName: String){
//        val warehouseName = db.getReference("Warehouses/$obsoleteName/name")
//        warehouseName.setValue(newName)
//    }
//
//    fun deleteWarehouse(warehouseName: String){
//        val warehouse = db.getReference("Warehouse/$warehouseName")
//        warehouse.removeValue()
//    }
//
//    fun getWarehouseTypesOfProduct(warehouseName: String): ArrayList<Product>? {
//        val warehouse = db.getReference("Warehouse")
//        var list: ArrayList<Product>? = ArrayList()
//
//
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val warehouse = snapshot.getValue(Warehouse::class.java)
//
//                if (warehouse != null) {
//                    if (warehouse.name == warehouseName) {
//                        list = warehouse.products
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("My_Log", "LoadPost:onCancelled", error.toException())
//            }
//        }
//
//        warehouse.addListenerForSingleValueEvent(postListener)
//
//        return list
//    }
//
//    fun addWarehouseTypeOfProduct(warehouseName: String, productName: String){
//        val warehouse = db.getReference("Warehouse/$warehouseName/products")
//        val product = Product(productName, 0f)
//        var products = getWarehouseTypesOfProduct(warehouseName)
//
//
//        if (products != null) {
//            products.add(product)
//        }
//        else{
//            val list = ArrayList<Product>()
//            list.add(product)
//            products = list
//        }
//
//        warehouse.setValue(products)
//    }
//
//    fun changeAmountOfProduct(warehouseName: String, typeOfProduct: String, newAmountOfProduct: Float){
//        val warehouse = db.getReference("Warehouse/$warehouseName/products")
//        val products = getWarehouseTypesOfProduct(warehouseName)
//        if (products != null) {
//            for(i in 0 until products.size){
//                if (products.get(i).name == typeOfProduct){
//                    products.set(i, Product(typeOfProduct, newAmountOfProduct))
//                    break
//                }
//            }
//        }
//        warehouse.setValue(products)
//    }
//
//    fun changeNameOfProduct(warehouseName: String, typeOfProduct: String, newTypeOfProduct: String){
//        val warehouse = db.getReference("Warehouse/$warehouseName/products")
//        val products = getWarehouseTypesOfProduct(warehouseName)
//        if (products != null) {
//            for(i in 0 until products.size){
//                if (products.get(i).name == typeOfProduct){
//                    products.set(i, Product(newTypeOfProduct, products.get(i).amountOfProduct))
//                    break
//                }
//            }
//        }
//        warehouse.setValue(products)
//    }
//
////    fun photoProcessing(){
////        TODO(// сделать обработку фото //)
////    }
//
//}