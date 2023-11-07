package com.example.myapplication

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.components.EthernetIcon
import com.example.myapplication.ui.components.MyTopBar
import com.example.myapplication.ui.components.WifiIcon
import com.example.myapplication.usecases.GetNetworkStatusForDevicesUseCase.*
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlin.random.Random

@Composable
fun SettingsDeviceListScreen(
    onBackClick: () -> Unit,
    onDeviceClick: (locationId: String, deviceId: String, locationName: String) -> Unit,
    onConfigureNewHeartnwtworkClick: () -> Unit,
) {
    val viewModel: SettingsDeviceListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    val deviceListState by remember(uiState.isLoading, uiState.deviceList) {
//        mutableStateOf(
//            when {
//                uiState.isLoading -> DeviceListState.Loading
////                uiState.deviceList.isEmpty() -> DeviceListState.EmptyList
//                uiState.deviceList.isEmpty() -> DeviceListState.EmptyList
//                else -> DeviceListState.LoadedList(uiState.deviceList)
//            }
//        )
//    }

    SettingsDeviceListScreen(
        isLoading = uiState.isLoading,
        devices = uiState.deviceList,
//        deviceListState = deviceListState,
//        error = uiState.error,
        onBackClick = onBackClick,
//        onErrorShown = viewModel.error::clear,
        onDeviceClick = { device ->
            onDeviceClick(
                device.heartnetworkIdentifier.locationId,
                device.heartnetworkIdentifier.deviceId,
                device.locationName,
            )
        },
        onConfigureNewHeartnetworkClick = onConfigureNewHeartnwtworkClick,
    )
}

private sealed class DeviceListState {
    object Loading : DeviceListState()
    object EmptyList : DeviceListState()
    //    @Immutable
    data class LoadedList(val list: List<DeviceListItem>) : DeviceListState()
//    data class LoadedList(val list: SnapshotStateList<DeviceListItem>) : DeviceListState()
//    data class LoadedList(val list: ImmutableList<DeviceListItem>) : DeviceListState()
}

private object HeartnetworkSettingsDeviceListScreenTokens {
    val titleTextStyle @Composable get() = MaterialTheme.typography.headlineSmall.copy(
        fontSize = 34.sp,
        fontWeight = FontWeight(400),
    )
    val titleTextColor = Color(0xFF2C2C2C)
    val subtitleTextStyle @Composable get() = MaterialTheme.typography.headlineSmall.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight(400),
    )
    val subtitleTextColor = Color(0xFFA3A19F)
    val listTitleTextStyle @Composable get() = MaterialTheme.typography.bodySmall
    val listTitleColor = Color(0xFFA3A19F)
}

@Composable
private fun SettingsDeviceListScreen(
    isLoading: Boolean,
//    devices: List<DeviceListItem>,
    devices: Devices,
//    deviceListState: DeviceListState,
//    error: Throwable?,
    onBackClick: () -> Unit,
//    onErrorShown: () -> Unit,
    onDeviceClick: (DeviceListItem) -> Unit,
    onConfigureNewHeartnetworkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { paddingValues ->
//
//        error?.let { err ->
//            VoltsOnErrorDialog(
//                errorMessage = heartnetworkErrorMessage(err),
//                onDismissClicked = onErrorShown,
//            )
//        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(30.dp)
                .padding(paddingValues),
        ) {
            Column(
                modifier = modifier.weight(1f),
            ) {
                MyTopBar(
                    onBackClick = onBackClick,
                    isBackButtonDark = true,
                )
                Spacer(modifier = Modifier.height(52.dp))
                Text(
                    text = stringResource(id = R.string.heartnetwork_settings_deviceList_title),
                    style = HeartnetworkSettingsDeviceListScreenTokens.titleTextStyle,
                    color = HeartnetworkSettingsDeviceListScreenTokens.titleTextColor,
                )
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = stringResource(id = R.string.heartnetwork_settings_deviceList_description),
                    style = HeartnetworkSettingsDeviceListScreenTokens.subtitleTextStyle,
                    color = HeartnetworkSettingsDeviceListScreenTokens.subtitleTextColor,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(id = R.string.heartnetwork_settings_deviceList_listTitle),
                    style = HeartnetworkSettingsDeviceListScreenTokens.listTitleTextStyle,
                    color = HeartnetworkSettingsDeviceListScreenTokens.listTitleColor,
                )
                Spacer(modifier = Modifier.height(4.dp))
                DevicesList(
                    isLoading = isLoading,
                    devices = devices,
//                    deviceListState = deviceListState,
                    onDeviceClick = onDeviceClick,
                )

            }
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape),
                    onClick = onConfigureNewHeartnetworkClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C2C2C),
                        disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                ) {
                    Text(text = stringResource(R.string.heartnetwork_settings_deviceList_buttonTitle_configure))
                }
            }
        }
    }
}

private object DevicesListTokens {
    val backgroundColor = Color(0xFFEAE8E5)
    val shape = RoundedCornerShape(size = 12.dp)
    val minHeight = 150.dp
    val contentPadding = PaddingValues(all = 8.dp)
    val itemSpacing: Dp = 8.dp
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DevicesList(
    isLoading: Boolean,
//    devices: List<DeviceListItem>,
    devices: Devices,
//    deviceListState: DeviceListState,
    onDeviceClick: (DeviceListItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val placeholderDevices = remember {
        val placeHolderDevice = DeviceListItem(
            heartnetworkIdentifier = HeartnetworkIdentifier("", ""),
            locationName = "",
            networkConfiguration = NetworkState.Loading
        )
        listOf(placeHolderDevice, placeHolderDevice, placeHolderDevice)
    }

    Crossfade(
        targetState = isLoading,
        modifier = modifier.animateContentSize(),
        label = "Device List Loading/Not Loading Crossfade",
    ) { loading ->
        when (loading) {
            true -> DeviceListContainer {
                items(items = placeholderDevices) { device ->
                    DeviceComponent(
                        onDeviceClick = {},
                        device = device,
                        modifier = Modifier
                            .animateItemPlacement()
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = Color(0xFF2B2B2B)
                            )
                    )
                }
            }
//            DeviceListState.EmptyList ->  DeviceListContainer(content = {})
            false -> DeviceListContainer {
                items(
                    key = { it.heartnetworkIdentifier.deviceId + it.heartnetworkIdentifier.locationId },
                    items = devices.list,
                    itemContent = { device ->
                        Box(modifier = Modifier.animateItemPlacement()) {
                            DeviceComponent(
                                onDeviceClick = onDeviceClick,
                                device = device,
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DeviceListContainer(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = DevicesListTokens.minHeight)
            .clip(DevicesListTokens.shape)
            .background(DevicesListTokens.backgroundColor),
        contentPadding = DevicesListTokens.contentPadding,
        verticalArrangement = Arrangement.spacedBy(DevicesListTokens.itemSpacing),
        content = content
    )
}

private object DeviceComponentTokens {
    val height = 74.dp
    val backgroundColor = Color(0xFF252525)
    val textStyle @Composable get() = MaterialTheme.typography.bodyMedium
    val deviceNameColor = Color(0xFFFFFFFF)
    val shape = RoundedCornerShape(6.dp)
    val contentPadding: PaddingValues = PaddingValues(all = 16.dp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeviceComponent(
    onDeviceClick: (DeviceListItem) -> Unit,
    device: DeviceListItem,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = DeviceComponentTokens.shape,
        colors = CardDefaults.cardColors(
            containerColor = DeviceComponentTokens.backgroundColor,
//            containerColor = Color(Random.nextInt()),
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { onDeviceClick(device) },
        enabled = true,
        elevation = CardDefaults.cardElevation(
            defaultElevation =0.dp,
            pressedElevation =0.dp,
            focusedElevation =0.dp,
            hoveredElevation =0.dp,
            draggedElevation =0.dp,
            disabledElevation =0.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DeviceComponentTokens.height)
                .padding(DeviceComponentTokens.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.heartnetwork_deviceList_cell_title) + " " + device.locationName,
                    style = DeviceComponentTokens.textStyle,
                    color = DeviceComponentTokens.deviceNameColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Crossfade(
                targetState = device.networkConfiguration,
                label = "Network Status Crossfade",
            ) { networkConfigResult ->
                when (networkConfigResult) {
                    NetworkState.Offline -> WifiIcon(signalStrength = null)
                    NetworkState.Unknown -> {}
                    NetworkState.Ethernet -> EthernetIcon()
                    NetworkState.Loading -> CircularProgressIndicator(
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFFFFFFFF),
                    )
                    is NetworkState.Wifi -> WifiIcon(signalStrength = networkConfigResult.signalStrength)
                }
            }
        }
    }
}