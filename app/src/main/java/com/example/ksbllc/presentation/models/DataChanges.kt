package com.example.ksbllc.presentation.models

data class DataModel(val date: String, val name: String, val surname: String, val nameOfWarehouse: String,
                       val photoPath: String, val valueChanges: Float, val typeOfChanges: String,
                     val typeOfProduct: String)

// что должно быть
// date
// Name and surname
// Photo path
// value of changes
// type of changes: +/-
// type of product, which changed
