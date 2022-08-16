package com.example.ksbllc.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksbllc.R
import com.example.ksbllc.presentation.models.ProductItemModel

class ComposableFunctions{

    @Composable
    fun Top(){
        val TopColor = colorResource(id = R.color.top)
        TopAppBar(title = {
            Text(text = "KSB LLC", fontSize = 22.sp)},
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = null)
            }, backgroundColor = TopColor)
    }

    @Composable
    fun TwoCircles(){
        val circle_1 = colorResource(id = R.color.circle_for_background)
        val circle_2 = colorResource(id = R.color.circle_for_background2)
        Canvas(modifier = Modifier.size(200.dp),
            onDraw = {
                drawCircle(color = circle_1, center = Offset(200f, 200f))
                drawCircle(color = circle_2, center = Offset(0f, 350f))
            })
    }
}
