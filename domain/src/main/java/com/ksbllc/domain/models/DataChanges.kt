package com.ksbllc.domain.models

data class DataChanges(val date: String, val name: String, val surname: String, val nameOfWarehouse: String,
                       val photoPath: String, val valueChanges: Float, val typeOfChanges: String,
                       val typeOfProduct: String)
