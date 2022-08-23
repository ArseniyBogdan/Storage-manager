package com.example.ksbllc.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.ksbllc.R
import com.example.ksbllc.presentation.viewModels.AuthentificationActivityVM
import com.example.ksbllc.presentation.viewModels.MainActivityVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthentificationActivity : ComponentActivity() {
    private val composableFunctions: ComposableFunctions = ComposableFunctions()

    private val vm by viewModel<AuthentificationActivityVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        vm.flagSI.observe(this, Observer {
            if(vm.flagSI.value == true) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        vm.flagError.observe(this, Observer {
            if(vm.flagError.value != ""){
                Toast.makeText(this, vm.flagError.value, Toast.LENGTH_SHORT).show()
                vm.flagError.value = ""
            }
        })

        super.onCreate(savedInstanceState)
        setContent {
             Surface(color = MaterialTheme.colors.background) {
                 composableFunctions.TwoCircles()
                 composableFunctions.Top()
                 AuthentificationBody()
             }
        }
    }

    @Composable
    private fun AuthentificationBody(){
        val email = remember{ mutableStateOf("") }
        val password = remember{ mutableStateOf("") }
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "С возвращением!", fontSize = 28.sp,
                modifier = Modifier.padding(top = 100.dp))

            OutlinedTextField(value = email.value, placeholder =
            {Text(text = "Email", fontSize = 22.sp, color= Color.Gray)}, maxLines = 1,
                onValueChange = {newText -> email.value = newText},
                textStyle = TextStyle(fontSize = 22.sp),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(0.75f))

            OutlinedTextField(value = password.value, placeholder =
            {Text(text = "Пароль", fontSize = 22.sp, color= Color.Gray)}, maxLines = 1,
                onValueChange = {newText -> password.value = newText},
                textStyle = TextStyle(fontSize = 22.sp),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(0.75f))

            Button(onClick = {
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                val job = scope.launch {
                    vm.signIn(email.value, password.value)
                }
            },
                modifier = Modifier.padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource
                    (id = R.color.button_confirm_bege))) {
                Box(modifier = Modifier
                    .fillMaxWidth(0.75f),
                    contentAlignment = Alignment.Center){
                    Text(text = "Подтвердить", fontSize = 22.sp)
                }
            }
        }
    }
}