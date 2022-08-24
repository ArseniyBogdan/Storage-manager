package com.example.ksbllc.presentation

import android.content.Context
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
import androidx.lifecycle.lifecycleScope
import com.example.ksbllc.R
import com.example.ksbllc.presentation.viewModels.RegistrationActivityVM
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : ComponentActivity() {
    private val composableFunctions: ComposableFunctions = ComposableFunctions()

    private val vm by viewModel<RegistrationActivityVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        val prefs = getSharedPreferences("pref", MODE_PRIVATE)

        vm.flagReg.observe(this, Observer {
            if(vm.flagReg.value == true){
                Toast.makeText(context, "Пользователь добавлен!", Toast.LENGTH_SHORT).show()
                prefs.edit().putBoolean("firstRun", true).apply()

                startActivity(Intent(context, AuthentificationActivity::class.java))
            }
        })

        vm.flagError.observe(this, Observer {
            if (vm.flagError.value != ""){
                Toast.makeText(context, vm.flagError.value, Toast.LENGTH_SHORT).show()
                vm.flagError.value = ""
            }
        })

        setContent {
            Surface(color = MaterialTheme.colors.background) {
                composableFunctions.TwoCircles()
                Column(modifier = Modifier.fillMaxSize()) {
                    composableFunctions.Top()
                    RegistrationBody(context)
                }
            }
        }
    }

    @Composable
    private fun RegistrationBody(context: Context){
        val firstName = remember{ mutableStateOf("") }
        val secondName = remember{ mutableStateOf("") }
        val email = remember{ mutableStateOf("") }
        val password = remember{ mutableStateOf("") }
        val passwordConfirm = remember{ mutableStateOf("") }
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "Добро пожаловать", fontSize = 28.sp,
                modifier = Modifier.padding(top = 100.dp))
            Text(text = "Введите данные для регистрации", fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 5.dp))

            OutlinedTextField(value = firstName.value, placeholder = {
                Text(text = "Имя", fontSize = 22.sp, color= Color.Gray)}, maxLines = 1,
                onValueChange = {newText -> firstName.value = newText},
                textStyle = TextStyle(fontSize = 22.sp),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(0.75f))

            OutlinedTextField(value = secondName.value, placeholder =
            {Text(text = "Фамилия", fontSize = 22.sp, color= Color.Gray)}, maxLines = 1,
                onValueChange = {newText -> secondName.value = newText},
                textStyle = TextStyle(fontSize = 22.sp),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(0.75f))

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

            OutlinedTextField(value = passwordConfirm.value, placeholder =
            {Text(text = "Подтверждение пароля", fontSize = 22.sp, color= Color.Gray)}, maxLines = 1,
                onValueChange = {newText -> passwordConfirm.value = newText},
                textStyle = TextStyle(fontSize = 22.sp),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(0.75f))

            Button(onClick = {
                lifecycleScope.launch {
                    vm.registration(firstName.value, secondName.value, email.value, password.value,
                        passwordConfirm.value)
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