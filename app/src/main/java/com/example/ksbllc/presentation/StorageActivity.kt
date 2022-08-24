package com.example.ksbllc.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ksbllc.R
import com.example.ksbllc.presentation.models.ProductItemModel
import com.example.ksbllc.presentation.ui.theme.KSBLLCTheme
import com.example.ksbllc.presentation.viewModels.MainActivityVM
import com.example.ksbllc.presentation.viewModels.RegistrationActivityVM
import com.example.ksbllc.presentation.viewModels.StorageActivityVM
import com.ksbllc.domain.models.Warehouse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorageActivity : ComponentActivity() {
    private val vm by viewModel<StorageActivityVM>()
    private lateinit var warehouseName: String
    val cf = ComposableFunctions()
    override fun onCreate(savedInstanceState: Bundle?) {
        val i = Intent()
        warehouseName = i.getStringExtra("WarehouseName").toString()

        super.onCreate(savedInstanceState)
        setContent {
            KSBLLCTheme {
                val products = remember{mutableStateOf(ArrayList<ProductItemModel>())}

                LaunchedEffect(key1 = Unit, block = {
                    products.value =  vm.getAllProducts(warehouseName)
                })

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    cf.Top()
                    LazyColumn {
                        itemsIndexed(
                            products.value
                        ){ index, product ->
                            ProductItem(product)
                        }
                    }

                    val sDialog = remember{mutableStateOf(value = false)}

                    if (sDialog.value){
                        CreateAddProductDialog(sDialog, products)
                    }

                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(40.dp)){
                        FloatingActionButton(onClick = {
                            sDialog.value = true
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "Добавить")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ProductItem(product: ProductItemModel){
        val rDialog = remember { mutableStateOf(value = false)}

        if(rDialog.value){
            CreateResetAmountOfProductDialog(openDialog = rDialog, product)
        }

        Card(elevation = 3.dp, shape = RoundedCornerShape(5.dp),
            contentColor = colorResource(id = R.color.card_color),
            modifier = Modifier.clickable {
                rDialog.value = true
            }) {
            // надо будет добавить по иконке Edit
            Column() {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
                    Text(text = product.typeOfProduct, fontSize = 24.sp, color = Color.Black)
                }
                Divider(thickness = 2.dp, color = Color.Black,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                Text(text = "Нетто: ${product.amountOfProduct_Netto} кг",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    fontSize = 22.sp,
                    color = Color.Black)
                Row{
                    Text(text = "Брутто: ${product.amountOfProduct_Brutto} кг",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(5.dp), color = Color.Black)
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                        contentAlignment = Alignment.BottomEnd){
                        Text(text = "Изменить", color = colorResource(id = R.color.change_color), fontSize = 22.sp)
                    }
                }
            }
        }

    }

    @Composable
    fun CreateAddProductDialog(openDialog: MutableState<Boolean>,
                               products: MutableState<ArrayList<ProductItemModel>>){
        val message = remember{ mutableStateOf("")}

        vm.flagAdd.observe(this, Observer {
            if(vm.flagAdd.value == true){
                Toast.makeText(this, "Элемент добавлен в базу", Toast.LENGTH_SHORT).show()
                products.value.add(ProductItemModel(message.value, 0f, 0f))
            }
        })

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Вы хотите добавить продукт?")},
            text = {
                TextField(value = message.value,
                    onValueChange = {newText -> message.value = newText})
            },
            buttons = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.End){
                    Button(onClick = { openDialog.value = false }) {
                        Text(text = "Отменить")
                    }
                    Button(onClick = {
                        val scope = CoroutineScope(Job() + Dispatchers.Main)
                        val job = scope.launch {
                            val product = ProductItemModel(message.value, 0f, 0f)
                            vm.addProduct(nameOfWarehouse = warehouseName, product = product)
                            openDialog.value = false
                        }
                    }) {
                        Text(text = "Подтвердить")
                    }
                }
            })
    }


    @Composable
    fun CreateResetAmountOfProductDialog(openDialog: MutableState<Boolean>, product: ProductItemModel){
        // TODO("сделать проверку на netto<=brutto")

        val nettoChange = remember { mutableStateOf("")}
        val bruttoChange = remember { mutableStateOf("")}

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = null,
            text = {
                Column {
                    Text(
                        text = product.typeOfProduct,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(start = 5.dp, top = 5.dp, end = 5.dp),
                        color = Color.Black
                    )
                    Divider(
                        color = Color.Black,
                        thickness = 2.dp,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth()){
                        TextField(value = nettoChange.value,
                            onValueChange = { newText -> nettoChange.value = newText },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(4f)
                                .padding(5.dp),
                            textStyle = TextStyle(fontSize = 22.sp)
                        )
                        Text(text = "кг" , fontSize = 22.sp, modifier = Modifier.padding(5.dp))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(value = bruttoChange.value,
                            onValueChange = { newText -> bruttoChange.value = newText },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(5.dp),
                            textStyle = TextStyle(fontSize = 22.sp)
                        )
                        Text(text = "кг" ,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }

            },
            buttons = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.Center){
                    Button(onClick = {
                        if (vm.checkCorrectness(nettoChange.value, bruttoChange.value)){
                            val scope = CoroutineScope(Job() + Dispatchers.Main)
                            val job = scope.launch {
                                vm.withdrowAmountOfProduct(nameOfWarehouse = warehouseName,
                                    product, nettoChange.value.toFloat(), bruttoChange.value.toFloat())
                                openDialog.value = false
                            }
                        }
                    })
                    {
                        Text(text = "Изъять")
                    }
                    Button(onClick = {
                        if (vm.checkCorrectness(nettoChange.value, bruttoChange.value)){
                            val scope = CoroutineScope(Job() + Dispatchers.Main)
                            val job = scope.launch {
                                vm.addAmountOfProduct(nameOfWarehouse = warehouseName,
                                    product, nettoChange.value.toFloat(), bruttoChange.value.toFloat())
                                openDialog.value = false
                            }
                        }
                    })
                    {
                        Text(text = "Добавить")
                    }
                }
            })

    }

    // диалог по удалению

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        KSBLLCTheme {
            ProductItem(ProductItemModel("kemk", 100f, 120f))
        }
    }
}

