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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ksbllc.R
import com.example.ksbllc.presentation.models.ProductItemModel
import com.ksbllc.domain.models.Warehouse
import com.example.ksbllc.presentation.viewModels.MainActivityVM
import com.example.ksbllc.ui.theme.KSBLLCTheme
import com.ksbllc.domain.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val composableFunctions: ComposableFunctions = ComposableFunctions()
    private lateinit var context: MainActivity

    private lateinit var vm: MainActivityVM

    override fun onRestart() {
        super.onRestart()
        vm.flagRestart.value = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        vm = ViewModelProvider(this).get(MainActivityVM::class.java)

        super.onCreate(savedInstanceState)
        setContent {
            val warehouses = remember{ mutableStateOf(ArrayList<Warehouse>()) }
            var accessLVL =  remember{ mutableStateOf("administrator") }
            LaunchedEffect(key1 = Unit, block = {
                accessLVL.value =  vm.getAccessLVL()
                warehouses.value = vm.getAllWarehouses(accessLVL.value)
            })

            // update list of warehouses, when activity restarts
            vm.flagRestart.observe(this, Observer {
                if(vm.flagRestart.value == true){
                    val scope = CoroutineScope(Job() + Dispatchers.Main)
                    val job = scope.launch {
                        accessLVL.value =  vm.getAccessLVL()
                        warehouses.value = vm.getAllWarehouses(accessLVL.value)
                    }
                    vm.flagRestart.value = false
                }
            })

            context = this

            KSBLLCTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column() {
                        composableFunctions.Top()
                        LazyColumn(){
                            itemsIndexed(
                                warehouses.value
                            ){ index, warehouse ->
                                val products = vm.createListOfProducts(warehouse)
                                StorageItem(warehouse = warehouse, items = products, warehouses = warehouses)
                            }
                        }

                    }
                    if(accessLVL.value == "administrator"){
                        Box(
                            contentAlignment = Alignment.TopEnd,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)){
                            Icon(Icons.Filled.Settings, contentDescription = "Добавить",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        startActivity(Intent(context, AccessLVLActivity::class.java))
                                    })
                        }
                    }

                    // может сделать его в сreateAddStorageDialog ?
                    val sDialog = remember{mutableStateOf(value = false)}

                    if (sDialog.value){
                        CreateAddStorageDialog(sDialog, warehouses)
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
    private fun CreateAddStorageDialog(openDialog: MutableState<Boolean>,
                               warehouses: MutableState<ArrayList<Warehouse>>){
        val message = remember{ mutableStateOf("")}

        vm.flagAdd.observe(this, Observer {
            if(vm.flagAdd.value == true){
                Toast.makeText(this, "Элемент добавлен в базу", Toast.LENGTH_SHORT).show()
                warehouses.value.add(Warehouse(message.value, 10000f))
            }
        })

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Вы хотите добавить склад?")},
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
                            vm.createNewWarehouse(storageName = message.value, products = arrayListOf())
                            openDialog.value = false
                        }
                    }) {
                        Text(text = "Подтвердить")
                    }
                }
            })
    }

    @Composable
    fun StorageItem(warehouse: Warehouse,
                     items: ArrayList<ProductItemModel>, warehouses: MutableState<ArrayList<Warehouse>>){
        val CardColor = colorResource(id = R.color.card_color)
        Card(shape = RoundedCornerShape(5.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp).
            clickable {
                startActivity(Intent(context, StorageActivity::class.java))
            },
            backgroundColor = CardColor,
            elevation = 4.dp
        ) {
            Column() {
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Box(modifier = Modifier
                        .height(30.dp)
                        .padding(start = 10.dp, top = 5.dp,)
                        .fillMaxWidth()
                        .weight(12f)){
                        Text(text = warehouse.name,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 20.sp)
                    }
                    Box(modifier = Modifier
                        .height(30.dp)
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .weight(1f)){
                        var expanded by remember { mutableStateOf(false)}
                        IconButton(onClick = {expanded = true}
                        ){
                            Icon(painter = painterResource(id = R.drawable.ic_menu),
                                contentDescription = null)
                        }

                        // для переименования склада
                        val rDialog = remember{mutableStateOf(value = false)}

                        if (rDialog.value){
                            CreateRenameWarehouseDialog(warehouse, rDialog, warehouses = warehouses)
                        }


                        // для удаления склада
                        val dDialog = remember{mutableStateOf(value = false)}

                        if (dDialog.value){
                            CreateDeleteWarehouseDialog(warehouse.name, dDialog, warehouses)
                        }

                        DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                rDialog.value = true
                            }) {
                                Text(text = "Переименовать склад")
                            }
                            DropdownMenuItem(onClick = {
                                expanded = false
                                dDialog.value = true
                            }) {
                                Text(text = "Удалить склад")
                            }
                        }
                    }
                }
                Text(text = "Общая масса: " + warehouse.capacity + " кг",
                    fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp, bottom = 5.dp))
                Column(){
                    for(item in items){
                        Text(text = "${item.typeOfProduct}: ${item.amountOfProduct_Netto} кг",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 5.dp, start = 10.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun CreateRenameWarehouseDialog(warehouse: Warehouse, rDialog: MutableState<Boolean>,
                                    warehouses: MutableState<ArrayList<Warehouse>>){
        val message = remember{ mutableStateOf(warehouse.name)}

        vm.flagRename.observe(this, Observer {
            if(vm.flagRename.value == true){
                Toast.makeText(this, "Склад переименован", Toast.LENGTH_SHORT).show()
                warehouses.value = vm.replaceWithNewName(warehouse.name, message.value, warehouses.value)
            }
        })

        AlertDialog(
            onDismissRequest = {
                rDialog.value = false
            },
            title = { Text(text = "Вы хотите переименовать склад?")},
            text = {
                TextField(value = message.value,
                    onValueChange = {newText -> message.value = newText})
            },
            buttons = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.End){
                    Button(onClick = { rDialog.value = false }) {
                        Text(text = "Отменить")
                    }
                    Button(onClick = {
                        val scope = CoroutineScope(Job() + Dispatchers.Main)
                        val job = scope.launch {
                            vm.renameWarehouse(warehouse.name, message.value, warehouse.capacity, warehouse.products)
                            rDialog.value = false
                        }
                    }) {
                        Text(text = "Подтвердить")
                    }
                }
            })
    }

    @Composable
    fun CreateDeleteWarehouseDialog(nameOFStorage: String, dDialog: MutableState<Boolean>,
                                    warehouses: MutableState<ArrayList<Warehouse>>){

        vm.flagDelete.observe(this, Observer {
            if(vm.flagDelete.value == true){
                Toast.makeText(this, "Склад удалён", Toast.LENGTH_SHORT).show()
                warehouses.value = vm.deleteWithName(nameOFStorage, warehouses.value)
            }
        })
        
        AlertDialog(
            onDismissRequest = {
                dDialog.value = false
            },
            title = { Text(text = "Вы точно хотите удалить $nameOFStorage?")},
            buttons = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.End){
                    Button(onClick = { dDialog.value = false }) {
                        Text(text = "Отменить")
                    }
                    Button(onClick = {
                        val scope = CoroutineScope(Job() + Dispatchers.Main)
                        val job = scope.launch {
                            vm.deleteWarehouse(nameOFStorage)
                            dDialog.value = false
                        }
                    }) {
                        Text(text = "Подтвердить")
                    }
                }
            })
    }
}

