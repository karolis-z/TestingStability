package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R


@Composable
fun EthernetIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_ethernet_on),
        contentDescription = null, // Decorative
        modifier = modifier.sizeIn(maxHeight = 24.dp),
        tint = Color.White,
    )
}
