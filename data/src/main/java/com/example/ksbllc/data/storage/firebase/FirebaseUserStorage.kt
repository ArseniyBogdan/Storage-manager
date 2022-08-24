package com.example.ksbllc.data.storage.firebase

import androidx.annotation.ArrayRes
import com.example.ksbllc.data.storage.UserStorage
import com.example.ksbllc.data.storage.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseUserStorage : UserStorage {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance("https://ksb-llc-default-rtdb.europe-west1.firebasedatabase.app/")
    private val users: DatabaseReference = db.getReference("Users")
    private val warehouses: DatabaseReference = db.getReference("Warehouses")

    override suspend fun registration(user: User): Boolean{
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {

                    val id = auth.currentUser?.uid

                    if (id != null) {
                        users.child(id).setValue(user).addOnSuccessListener {
                            continuation.resume(true)
                        }.addOnFailureListener { e ->
                            continuation.resume(false)
                        }
                    }
                }.addOnFailureListener { e ->
                    continuation.resume(false)
                }
        }
    }

    override suspend fun signIn(authentificationParams: AuthentificationParams): Boolean{
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(authentificationParams.email, authentificationParams.password).addOnSuccessListener {
                continuation.resume(true)
            }.addOnFailureListener {e ->
                continuation.resume(false)
            }
        }
    }

    override suspend fun getAccessLVL(): String {
        var accessLVL = "Неизвестно"
        val userEmail = auth.currentUser?.email

        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(dataSnapshot in snapshot.children){
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (user.email == userEmail){
                                accessLVL = user.accessLVL
                                continuation.resume(accessLVL)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(accessLVL)
                }
            }

            users.addListenerForSingleValueEvent(postListener)
        }
    }

    override suspend fun getAllWarehouses(accessValue: String): ArrayList<Warehouse> {
        val listOfWarehouses: ArrayList<Warehouse> = ArrayList()

        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(dataSnapshot in snapshot.children){
                        val warehouse = dataSnapshot.getValue(Warehouse::class.java)
                        if (warehouse != null) {
                            if (warehouse.name == accessValue || accessValue == "administrator") {
                                listOfWarehouses.add(warehouse)
                            }
                        }
                    }
                    continuation.resume(listOfWarehouses)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(listOfWarehouses)
                }
            }

            warehouses.addListenerForSingleValueEvent(postListener)
        }
    }

    override suspend fun createNewWarehouse(storageName: String, capacity: Float,
                                    products: ArrayList<Product>?): Boolean{
        val warehouse = Warehouse(storageName, capacity, products)

        return suspendCoroutine { continuation ->
            warehouses.child(storageName).setValue(warehouse).addOnSuccessListener {
                continuation.resume(true)
            }.addOnCanceledListener {
                continuation.resume(false)
            }
        }
    }

    override suspend fun renameWarehouse(obsoleteName: String, newName: String,
                                         capacity: Float, products: ArrayList<Product>?): Boolean {
        val result = deleteWarehouse(obsoleteName)

        if(!result){
            return false
        }

        val resultR = createNewWarehouse(newName, capacity = capacity, products = products)
        return resultR
    }

    override suspend fun deleteWarehouse(nameWarehouse: String): Boolean {
        return suspendCoroutine { continuation ->
            val warehouse = db.getReference("Warehouses/$nameWarehouse")
            warehouse.removeValue().addOnSuccessListener {
                continuation.resume(true)
            }.addOnFailureListener{
                continuation.resume(false)
            }
        }
    }

    override suspend fun getAllUsersAccessLVL(): ArrayList<AccessLVLUnitData>{
        return suspendCoroutine { continuation ->
            val unitList = ArrayList<AccessLVLUnitData>()

            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(dataSnapshot in snapshot.children){
                        val user = dataSnapshot.getValue(User::class.java)

                        if (user != null) {
                            unitList.add(AccessLVLUnitData(user.name, user.surname, user.accessLVL))
                        }
                    }
                    continuation.resume(unitList)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(arrayListOf(AccessLVLUnitData("", "", "administrator")))
                }
            }

            users.addListenerForSingleValueEvent(postListener)
        }
    }

    suspend fun getUserIDByAccessLVLUnitData(name: String, surname:String): String{
        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(dataSnapshot in snapshot.children){
                        val user = dataSnapshot.getValue(User::class.java)

                        if (user != null) {
                            if (user.name == name && user.surname == surname){
                                val key: String? = dataSnapshot.key
                                if(key != null){
                                    continuation.resume(key)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume("Error")
                }
            }

            users.addListenerForSingleValueEvent(postListener)
        }
    }

    override suspend fun resetAccessLVL(accessLVLUnitData: AccessLVLUnitData): Boolean {
        val userID = getUserIDByAccessLVLUnitData(name = accessLVLUnitData.name,
            surname = accessLVLUnitData.surname)
        return suspendCoroutine { continuation ->
            users.child(userID).child("accessLVL").setValue(accessLVLUnitData.accessLVL).addOnSuccessListener {
                continuation.resume(true)
            }.addOnFailureListener{
                continuation.resume(false)
            }
        }
    }

    override suspend fun addProduct(nameOFWarehouse: String, product: Product): Boolean {
        TODO("Перепроверить путь до продуктов")
        return suspendCoroutine { continuation ->
            warehouses.child(nameOFWarehouse).child("products").child(product.name).setValue(product).addOnSuccessListener {
                continuation.resume(true)
            }.addOnCanceledListener {
                continuation.resume(false)
            }
        }
    }

    override suspend fun getAllProducts(nameOFWarehouse: String): ArrayList<Product> {
        TODO("Перепроверить всё")

        return suspendCoroutine { continuation ->
            val products = ArrayList<Product>()


            val postLIstener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children){
                        val product = data.getValue(Product::class.java)

                        if(product != null){
                            products.add(product)
                        }
                    }
                    continuation.resume(products)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(products)
                }
            }
            warehouses.child(nameOFWarehouse).child("products").addListenerForSingleValueEvent(postLIstener)
        }
    }

    override suspend fun changeAmountOfProduct(
        nameOFWarehouse: String,
        product: Product
    ): Boolean {
        return suspendCoroutine { continuation ->
            // обновить продукт
            warehouses.child(nameOFWarehouse).child(product.name).setValue(product).addOnSuccessListener {
                continuation.resume(true)
            }.addOnFailureListener {
                continuation.resume(false)
            }
        }
    }

    override suspend fun sendPhotoAboutChange(photo: Any): Boolean {
        TODO("А вот тут мои полномочия сворачиваются")
    }

}