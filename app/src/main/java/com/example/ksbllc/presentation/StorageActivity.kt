package com.example.ksbllc.presentation

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.ksbllc.R
import com.example.ksbllc.presentation.models.ProductItemModel
import com.example.ksbllc.presentation.ui.theme.KSBLLCTheme
import com.example.ksbllc.presentation.viewModels.StorageActivityVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorageActivity : ComponentActivity() {
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private var flagPhoto: Boolean = false
    private val REQUEST_TAKE_PHOTO = 1

    private val vm by viewModel<StorageActivityVM>()
    private lateinit var warehouseName: String
    val cf = ComposableFunctions()
    override fun onCreate(savedInstanceState: Bundle?) {
        registerPermissionListener()
        flagPhoto = checkCameraPermission()

        val intent = intent.extras
        if (intent != null) {
            warehouseName = intent.getString("WarehouseName").toString()
        }

        super.onCreate(savedInstanceState)
        setContent {
            KSBLLCTheme {
                val products = remember{mutableStateOf(ArrayList<ProductItemModel>())}

                LaunchedEffect(key1 = Unit, block = {
                    products.value =  vm.getAllProducts(warehouseName)
                })

                vm.flagAdd.observe(this, Observer {
                    if(vm.flagAdd.value == true){
                        lifecycleScope.launch(Dispatchers.IO){
                            products.value =  vm.getAllProducts(warehouseName)
                        }
                    }
                })

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column() {
                        cf.Top()
                        LazyColumn {
                            itemsIndexed(
                                products.value
                            ){ index, product ->
                                ProductItem(product)
                            }
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
            contentColor = colorResource(id = R.color.card_color)) {
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
                        Text(
                            text = "Изменить",
                            color = colorResource(id = R.color.change_color),
                            fontSize = 22.sp,
                            modifier = Modifier.clickable {
                                rDialog.value = true
                            })
                    }
                }
            }
        }

    }

    @Composable
    fun CreateAddProductDialog(openDialog: MutableState<Boolean>,
                               products: MutableState<ArrayList<ProductItemModel>>){
        val message = remember{ mutableStateOf("")}

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
                        lifecycleScope.launch {
                            val productList = ArrayList<ProductItemModel>()
                            for(product in products.value){
                                productList.add(product)
                            }
                            val product = ProductItemModel(message.value, 0f, 0f)
                            productList.add(product)
                            vm.addProduct(nameOfWarehouse = warehouseName, products = productList)
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
        val nettoChange = remember { mutableStateOf("")}
        val bruttoChange = remember { mutableStateOf("")}
        val flagAddPhoto = remember { mutableStateOf(false)}
        val text = remember{ mutableStateOf("")}

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
                    Box(modifier = Modifier
                        .fillMaxWidth(), contentAlignment = Alignment.BottomCenter){
                        Row(modifier = Modifier.fillMaxWidth()){
                            OutlinedTextField(
                                value = nettoChange.value,
                                placeholder =
                                { Text(text = "Нетто", fontSize = 22.sp, color = Color.Gray) },
                                maxLines = 1,
                                onValueChange = { newText -> nettoChange.value = newText },
                                textStyle = TextStyle(fontSize = 22.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(5.dp)
                                    .background(color = colorResource(R.color.card_color))
                            )
                            Text(
                                text = "кг",
                                fontSize = 26.sp,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .weight(1f)
                                    .alignByBaseline()
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = bruttoChange.value, placeholder =
                        {Text(text = "Брутто", fontSize = 22.sp, color= Color.Gray) }, maxLines = 1,
                            onValueChange = {newText -> bruttoChange.value = newText},
                            textStyle = TextStyle(fontSize = 22.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f)
                                .padding(5.dp)
                                .background(color = colorResource(R.color.card_color)))
                        Text(text = "кг" ,
                            fontSize = 26.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f)
                        )
                    }

                    vm.flagSetPhoto.observe(this@StorageActivity, Observer {
                        if(vm.flagSetPhoto.value == true){
                            Log.d("Debug", flagAddPhoto.value.toString())
                            flagAddPhoto.value = true
                            vm.flagSetPhoto.value = false
                            Log.d("Debug", "Я ставлю флаг на true")
                            Log.d("Debug", flagAddPhoto.value.toString())
                            Log.d("Debug", vm.flagSetPhoto.hasObservers().toString())
                        }
                    })

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                    {
                        Button(onClick = {
                            if (flagPhoto) {
                                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                try {
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                                } catch (e: ActivityNotFoundException) {
                                    e.printStackTrace()
                                }
                            }
                            if (!flagPhoto) {
                                Toast.makeText(
                                    this@StorageActivity,
                                    "Вы не дали разрешение на использование 'Камера'",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }) {
                            Text(
                                text = "Сделать фото",
                                modifier = Modifier.padding(2.dp),
                                fontSize = 22.sp
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), contentAlignment = Alignment.Center){
                        Text(
                            text = text.value,
                            fontSize = 22.sp,
                            color = Color.Green
                        )
                    }

                    if(flagAddPhoto.value){
                        Log.d("Debug", "Поставил текст")
                        text.value = "Фото сделано"
                        flagAddPhoto.value = false
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
                            lifecycleScope.launch {
                                vm.withdrowAmountOfProduct(nameOfWarehouse = warehouseName,
                                    product, nettoChange.value.toFloat(), bruttoChange.value.toFloat())
                                openDialog.value = false
                            }
                        }
                    }, modifier = Modifier.padding(end = 10.dp))
                    {
                        Text(text = "Изъять")
                    }
                    Button(onClick = {
                        if (vm.checkCorrectness(nettoChange.value, bruttoChange.value)){
                            lifecycleScope.launch {
                                vm.addAmountOfProduct(nameOfWarehouse = warehouseName,
                                    product, nettoChange.value.toFloat(), bruttoChange.value.toFloat())
                                openDialog.value = false
                            }
                        }
                    }, modifier = Modifier.padding(start = 10.dp))
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
        val nettoChange = remember {
            mutableStateOf("")
        }
        KSBLLCTheme {
            TextField(value = nettoChange.value,
                onValueChange = { newText -> nettoChange.value = newText },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textStyle = TextStyle(fontSize = 18.sp)
            )
            Text(text = "кг" , fontSize = 22.sp, modifier = Modifier.padding(5.dp))
        }
    }

    private fun checkCameraPermission(): Boolean{
        when{
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED ->{
                return true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    this,
                    "Нам нужно это разрешение для работы с фотографиями",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            else -> {
                pLauncher.launch(Manifest.permission.CAMERA)
                return false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_TAKE_PHOTO ->{
                if(resultCode == Activity.RESULT_OK && data !== null){
                    val thumbnailBitmap = data.extras?.get("data") as Bitmap
                    vm.setPhoto(thumbnailBitmap)
                }
            }
            else ->{
                Toast.makeText(this, "Фото не удалось сделать", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun registerPermissionListener(){
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            if(it){
                Toast.makeText(this, "Камера запущена", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Разрешение отклонено", Toast.LENGTH_SHORT).show()
            }
        }
    }

}