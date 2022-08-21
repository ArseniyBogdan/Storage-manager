package com.example.ksbllc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksbllc.presentation.models.ProductItemModel
import com.example.ksbllc.presentation.ui.theme.KSBLLCTheme

class StorageActivity : ComponentActivity() {
    val cf = ComposableFunctions()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KSBLLCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    cf.Top()
                    Greeting("Android")
                }
            }
        }
    }

    @Composable
    fun ProductItem(product: ProductItemModel){
        Card(elevation = 3.dp, shape = RoundedCornerShape(5.dp)) {
            // надо будет добавить по иконке Edit
            Column() {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
                    Text(text = product.typeOfProduct, fontSize = 22.sp)
                }
                Divider(thickness = 2.dp, color = Color.Black,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                Text(text = "Брутто: ${product.amountOfProduct_Brutto} кг",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp))
                Text(text = "Брутто: ${product.amountOfProduct_Netto} кг",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp))
            }
            Divider(thickness = 2.dp, color = Color.Black)
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KSBLLCTheme {
        Greeting("Android")
    }
}