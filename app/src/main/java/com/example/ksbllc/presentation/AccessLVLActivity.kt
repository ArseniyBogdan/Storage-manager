package com.example.ksbllc.presentation

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.ksbllc.R
import com.example.ksbllc.presentation.ui.theme.KSBLLCTheme
import com.example.ksbllc.presentation.viewModels.AccessLVLActivityVM
import com.ksbllc.domain.models.AccessLVLUnit
import com.ksbllc.domain.models.Warehouse
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccessLVLActivity : ComponentActivity() {
    private val composableFun = ComposableFunctions()
    private val vm by viewModel<AccessLVLActivityVM>()
    private var strSelectedData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val aLVLUnit = remember{ mutableStateOf(ArrayList<AccessLVLUnit>()) }
            val visibleTrashCan = remember{ mutableStateOf(false)}
            val counter = remember{ mutableStateOf(0)}
            vm.fetchAllWarehouses()
            val warehouseNames by vm.warehouses.observeAsState(initial = emptyList())
            LaunchedEffect(key1 = Unit, block = {
                aLVLUnit.value = vm.getAllWorkersAccessLVL()
            })

            vm.flagDelete.observe(this, Observer {
                if(vm.flagDelete.value == true){
                    Toast.makeText(this, "Пользователь удалён", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        aLVLUnit.value = vm.getAllWorkersAccessLVL()
                    }
                    counter.value = 0
                    vm.flagDelete.value = false
                }
            })

            vm.flagReset.observe(this, Observer {
                if(vm.flagReset.value == true){
                    Toast.makeText(this, "Уровень доступа изменён", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
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
                                    accessLVL = item.accessLVL, counter, warehouseNames)
                            }

                        }
                    }
                }
            }

            vm.flagVisible.observe(this, Observer {
                visibleTrashCan.value = vm.flagVisible.value == true
            })

            val dDialog = remember { mutableStateOf(false) }

            if(dDialog.value){
                DeleteUsersDialog(dDialog = dDialog)
            }

            if(counter.value > 0){
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)){
                    Icon(
                        Icons.Filled.Settings, contentDescription = "Добавить",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                dDialog.value = true
                            })
                }
            }
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ALVLItem(name: String, surname: String, accessLVL: String, counter: MutableState<Int>, warehouseNames: List<String>){
        val rDialog = remember {mutableStateOf(false)}
        val backColorID = remember{ mutableStateOf(R.color.card_color)}
        val accessLVLr = remember{ mutableStateOf(accessLVL)}

        // при каждом изменении уровня доступа, надо обновлять список
        if(accessLVLr.value != accessLVL){
            accessLVLr.value = accessLVL
        }

        if(counter.value == 0){
            backColorID.value = R.color.card_color
        }

        if(rDialog.value){
            CreateAccessDialog(name = name, surname = surname,
                accessLVL = accessLVLr, rDialog = rDialog, warehouseNames)
        }

        val context = this
        Column(modifier = Modifier.background(color = colorResource(id = backColorID.value))) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
                .combinedClickable(
                    onClick = {
                        // настройка цвета заднего плана у ячейки (выделена/не выделена)
                        if (backColorID.value != R.color.selected_color && counter.value == 0) {
                            // флаг для создания диалогового окна
                            rDialog.value = true
                        } else if (backColorID.value != R.color.selected_color) {
                            backColorID.value = R.color.selected_color
                            counter.value += 1
                            vm.selectUser(name, surname, accessLVL)
                            Toast
                                .makeText(context, counter.value.toString(), Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            backColorID.value = R.color.card_color
                            counter.value -= 1
                            vm.unselectUser(name, surname, accessLVL)
                            Toast
                                .makeText(context, counter.value.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    onLongClick = {
                        if (backColorID.value != R.color.selected_color) {
                            backColorID.value = R.color.selected_color
                            counter.value += 1
                            vm.selectUser(name, surname, accessLVL)
                            Toast
                                .makeText(context, counter.value.toString(), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            backColorID.value = R.color.card_color
                            counter.value -= 1
                            vm.unselectUser(name, surname, accessLVL)
                            Toast
                                .makeText(context, counter.value.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                )){
                Column() {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "$surname $name",
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                            fontSize = 22.sp)
                    }
                    Text(text = "Уровень доступа: $accessLVL",
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp, bottom = 10.dp),
                        fontSize = 22.sp)
                }
            }
            Divider(color = androidx.compose.ui.graphics.Color.Black, thickness = 2.dp)
        }

    }

    @Composable
    fun CreateAccessDialog(name: String, surname: String,
                           accessLVL: MutableState<String>, rDialog: MutableState<Boolean>,
                           warehouseNames: List<String>){

        val firstName = remember { mutableStateOf(name)}
        val secondName = remember { mutableStateOf(surname)}
        val accessLVLD = remember { mutableStateOf(accessLVL.value)}

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
                    TextFieldWithDropdownUsage(dataIn = warehouseNames)
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
                        lifecycleScope.launch {
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
    fun DeleteUsersDialog(dDialog: MutableState<Boolean>){
        AlertDialog(
            onDismissRequest = {
                dDialog.value = false
            },
            title = { androidx.compose.material.Text(text = "Вы точно хотите удалить этих пользователей?") },
            buttons = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.End){
                    Button(onClick = { dDialog.value = false }) {
                        androidx.compose.material.Text(text = "Отменить")
                    }
                    Button(onClick = {
                        lifecycleScope.launch {
                            vm.deleteUsers()
                            dDialog.value = false
                        }
                    }) {
                        androidx.compose.material.Text(text = "Подтвердить")
                    }
                }
            })
    }

    @Composable
    fun TextFieldWithDropdownUsage(dataIn: List<String>){
        val dropDownOptions = remember { mutableStateOf(listOf<String>())}
        val textFieldValue = remember { mutableStateOf(TextFieldValue())}
        val dropDownExpanded = remember { mutableStateOf(false)}

        fun onDropdownDismissRequest(){
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue){
            strSelectedData = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = dataIn.filter {
                it.startsWith(value.text) && it != value.text
            }
        }

        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = "Уровень доступа"
        )
    }

    @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<String>,
        label: String = ""
    ){
        Box(modifier){
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = { Text(text = label)},
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                list.forEach{ text ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text,
                                TextRange(text.length)
                            )
                        )
                    }) {
                        Text(text = text)
                    }
                }
            }
        }
    }

}

