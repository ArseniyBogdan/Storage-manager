package com.example.ksbllc.presentation

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ksbllc.R
import com.example.ksbllc.presentation.ui.theme.KSBLLCTheme
import com.example.ksbllc.presentation.viewModels.AccessLVLActivityVM
import com.example.ksbllc.presentation.viewModels.AuthentificationActivityVM
import com.example.ksbllc.presentation.viewModels.MainActivityVM
import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.Warehouse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccessLVLActivity : ComponentActivity() {
    private val composableFun = ComposableFunctions()
    private val vm by viewModel<AccessLVLActivityVM>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val aLVLUnit = remember{ mutableStateOf(ArrayList<AccessLVLUnit>()) }
            LaunchedEffect(key1 = Unit, block = {
                aLVLUnit.value = vm.getAllWorkersAccessLVL()
            })

            vm.flagReset.observe(this, Observer {
                if(vm.flagReset.value == true){
                    val scope = CoroutineScope(Job() + Dispatchers.Main)
                    val job = scope.launch {
                        aLVLUnit.value = vm.getAllWorkersAccessLVL()
                    }
                    vm.flagReset.value = false
                }
            })

            KSBLLCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        composableFun.Top()
                        LazyColumn{
                            itemsIndexed(aLVLUnit.value) { index, item ->
                                ALVLItem(name = item.name, surname = item.surname,
                                    accessLVL = item.accessLVL)
                            }

                        }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ALVLItem(name: String, surname: String, accessLVL: String){
        val rDialog = remember {mutableStateOf(false)}
        val backColorID = remember{ mutableStateOf(R.color.card_color)}
        val accessLVLr = remember{ mutableStateOf(accessLVL)}

//        vm.flagDelete.observe

        if(rDialog.value){
            CreateAccessDialog(name = name, surname = surname,
                accessLVL = accessLVLr, rDialog = rDialog)
        }

        Column(modifier = Modifier.background(color = colorResource(id = backColorID.value))) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
                .combinedClickable(
                    onClick = {
                        if(backColorID.value != R.color.selected_color){
                            rDialog.value = true
                        }
                        else{
                            backColorID.value = R.color.card_color
                        }},
                    onLongClick = {
                        if(backColorID.value != R.color.selected_color){
                            backColorID.value = R.color.selected_color
                        }
                        else{
                            backColorID.value =R.color.card_color
                        }}
                )){
                Column() {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = surname + " " + name,
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                            fontSize = 22.sp)
                    }
                    Text(text = "Уровень доступа: " + accessLVLr.value,
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp, bottom = 10.dp),
                        fontSize = 22.sp)
                }
            }
            Divider(color = androidx.compose.ui.graphics.Color.Black, thickness = 2.dp)
        }

    }

    @Composable
    fun CreateAccessDialog(name: String, surname: String,
                           accessLVL: MutableState<String>, rDialog: MutableState<Boolean>){

        val firstName = remember { mutableStateOf(name)}
        val secondName = remember { mutableStateOf(surname)}
        val accessLVLD = remember { mutableStateOf(accessLVL.value)}

        vm.flagReset.observe(this, Observer {
            if(vm.flagReset.value == true){
                Toast.makeText(this, "Уровень доступа изменён", Toast.LENGTH_SHORT).show()
                // нужно обновить самый первый список
                accessLVL.value = accessLVLD.value
                vm.flagReset.value = false
            }
        })

        AlertDialog(
            onDismissRequest = {
                rDialog.value = false
            },
            title = null,
            text = {
                Column() {
                    androidx.compose.material.Text(text = "Вы хотите переименовать склад?", fontSize = 18.sp)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        OutlinedTextField(value = secondName.value, placeholder =
                        { androidx.compose.material.Text(text = "Фамилия", fontSize = 22.sp, color= androidx.compose.ui.graphics.Color.Gray) }, maxLines = 1,
                            onValueChange = {newText -> secondName.value = newText},
                            textStyle = TextStyle(fontSize = 22.sp),
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth()
                                .padding(end = 5.dp))

                        OutlinedTextField(value = firstName.value, placeholder =
                        { androidx.compose.material.Text(text = "Имя", fontSize = 22.sp, color= androidx.compose.ui.graphics.Color.Gray) }, maxLines = 1,
                            onValueChange = {newText -> firstName.value = newText},
                            textStyle = TextStyle(fontSize = 22.sp),
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth()
                                .padding(start = 5.dp))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {

                        OutlinedTextField(value = accessLVLD.value, placeholder =
                        { androidx.compose.material.Text(text = "Уровень доступа", fontSize = 22.sp, color= androidx.compose.ui.graphics.Color.Gray) }, maxLines = 1,
                            onValueChange = {newText -> accessLVLD.value = newText},
                            textStyle = TextStyle(fontSize = 22.sp),
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth()
                                .padding(top = 10.dp))
//                        TODO("Сделать AutoCompleteEditText" +
//                                " сделать единый UseCase для замены всех данных")
                        
                    }
                }

            },
            buttons = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.End){
                    Button(onClick = { rDialog.value = false },
                        modifier = Modifier.padding(end = 10.dp)) {
                        androidx.compose.material.Text(text = "Отменить")
                    }
                    Button(onClick = {
                        val scope = CoroutineScope(Job() + Dispatchers.Main)
                        val job = scope.launch {
                            val LVLUnit = AccessLVLUnit(name, surname, accessLVLD.value)
                            vm.resetAccessLVL(LVLUnit)
                            rDialog.value = false
                        }
                    }) {
                        androidx.compose.material.Text(text = "Сохранить")
                    }
                }
            })
    }

    @Composable
    fun DeleteUserDialog(name: String, surname: String,
                         accessLVL: MutableState<String>, dDialog: MutableState<Boolean>){

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        KSBLLCTheme {
            ALVLItem("kemk", "The Second", "administrator")
        }
    }

}

