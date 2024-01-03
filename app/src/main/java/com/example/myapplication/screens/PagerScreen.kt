package com.example.myapplication.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PagerScreen(
    onBackClick: () -> Unit,
) {
    BackHandler {
        onBackClick()
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { fakeList.size }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pager Screen") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        ContentPager(
            pagerState = pagerState,
            utilities = fakeList,
            modifier = Modifier.padding(it),
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentPager(
    pagerState: PagerState,
    utilities: List<FakeDeviceUi>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        ContentCard(device = utilities[page])
    }
}

@Composable
private fun ContentCard(
    device: FakeDeviceUi,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(200.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = when (device) {
                        is FakeDeviceUi.Electricity -> Color.Cyan
                        is FakeDeviceUi.Heating -> Color.Yellow
                        is FakeDeviceUi.Water -> Color.Blue
                    }
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = device.javaClass.simpleName,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(8.dp))
            ValueText(floatValue = device.selectedConsumptionData?.consumptionAgainstBudget)
        }
    }
}

@Composable
private fun ValueText(
    floatValue: Float?,
) {
    LaunchedEffect(floatValue) {
        Log.d("TESTING", "ValueText with floatValue = $floatValue")
    }
    Text(
        text = "%.0f".format(floatValue?.times(100f)),
        style = MaterialTheme.typography.headlineLarge,
        color = Color.Black,
    )
}

val fakeList = listOf(
    FakeDeviceUi.Water(FakeConsumptionData(0.5f, Instant.now())),
    FakeDeviceUi.Electricity(FakeConsumptionData(1f, Instant.now())),
    FakeDeviceUi.Heating(FakeConsumptionData(1.5f, Instant.now())),
    FakeDeviceUi.Water(FakeConsumptionData(2f, Instant.now())),
    FakeDeviceUi.Heating(FakeConsumptionData(2.5f, Instant.now())),
)

// TODO: Temporary UI level implementation of Device with consumption
sealed class FakeDeviceUi(
    open val selectedConsumptionData: FakeConsumptionData? = null,
//    open val consumptionBubbleData: List<ConsumptionBubbleData> = emptyList(),
) {

    data class Water(
        override val selectedConsumptionData: FakeConsumptionData? = null,
//        override val consumptionBubbleData: List<ConsumptionBubbleData> = emptyList(),
    ) : FakeDeviceUi(
        selectedConsumptionData = selectedConsumptionData,
//        consumptionBubbleData = consumptionBubbleData,
    )

    data class Electricity(
        override val selectedConsumptionData: FakeConsumptionData? = null,
//        override val consumptionBubbleData: List<ConsumptionBubbleData> = emptyList(),
    ) : FakeDeviceUi(
        selectedConsumptionData = selectedConsumptionData,
//        consumptionBubbleData = consumptionBubbleData,
    )

    data class Heating(
        override val selectedConsumptionData: FakeConsumptionData? = null,
//        override val consumptionBubbleData: List<ConsumptionBubbleData> = emptyList(),
    ) : FakeDeviceUi(
        selectedConsumptionData = selectedConsumptionData,
//        consumptionBubbleData = consumptionBubbleData,
    )

}

//TODO: Temporary fake Domain level consumption data
data class FakeConsumptionData(
    val consumptionAgainstBudget: Float?,
    val date: Instant,
)
