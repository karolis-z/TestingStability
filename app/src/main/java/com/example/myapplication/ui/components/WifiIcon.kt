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
fun WifiIcon(
    signalStrength: Int?,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(
            id = when {
                signalStrength == null -> R.drawable.ic_voltson_wifi_off
                signalStrength <= 40 -> R.drawable.ic_voltson_wifi_bad
                signalStrength in 41..70 -> R.drawable.ic_voltson_wifi_average
                else -> R.drawable.ic_voltson_wifi_good
            },
        ),
        contentDescription = null, // TODO? Description based on signalStrength?
        modifier = modifier.sizeIn(maxHeight = 24.dp),
        tint = Color.White
    )
}
