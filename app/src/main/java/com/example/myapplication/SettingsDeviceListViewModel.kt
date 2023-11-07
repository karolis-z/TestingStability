package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.theme.Location
import com.example.myapplication.usecases.GetLocationsWithHeartnetworkDevicesOnlyUseCase
import com.example.myapplication.usecases.GetNetworkStatusForDevicesUseCase
import com.example.myapplication.usecases.GetNetworkStatusForDevicesUseCase.HeartnetworkIdentifier
import com.example.myapplication.usecases.NetworkConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

@HiltViewModel
class SettingsDeviceListViewModel @Inject constructor(
    getLocationsWithHeartnetworkDevicesOnlyUseCase: GetLocationsWithHeartnetworkDevicesOnlyUseCase,
    getNetworkStatusUseCase: GetNetworkStatusForDevicesUseCase,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val deviceList: Flow<Result<ImmutableList<DeviceListItem>>> =
        getLocationsWithHeartnetworkDevicesOnlyUseCase()
            .transformLatest { locations ->
                emitAll(
                    locations.toDeviceListItem()
                        .map { device ->
                            val delay = device.heartnetworkIdentifier.deviceId.takeLast(1).toIntOrNull()
                            getNetworkStatusUseCase(device.heartnetworkIdentifier, delay?.times(2) ?: 2)
                                .asResult()
                                .map(::mapNetworkConfigurationResultToNetworkState)
                                .map {
                                    device.heartnetworkIdentifier to device.copy(networkConfiguration = it)
                                }
                        }
                        .merge()
                        .runningFold(emptyMap<HeartnetworkIdentifier, DeviceListItem>()) { devices, item ->
                            devices + mapOf(item)
                        }
                        .map { it.values.toImmutableList() }
                )
            }
            .asResult()
            .flowOn(Dispatchers.Default)

//    val error by lazy {
//        Error(viewModelScope, deviceList)
//    }

    val uiState: StateFlow<HeartnetworkSettingsDeviceListUiState> = produceUiState(
        deviceList,
//        error,
        initialValue = HeartnetworkSettingsDeviceListUiState()
    ) { deviceList ->
        HeartnetworkSettingsDeviceListUiState(
            deviceList = (deviceList as? Result.Success)?.value ?: persistentListOf(),
            isLoading = deviceList is Result.Loading,
//            error = error,
        )
    }

    private fun List<Location>.toDeviceListItem(): ImmutableList<DeviceListItem> {
        val x = this.flatMap { location ->
            location.devices.map { device ->
                DeviceListItem(
                    heartnetworkIdentifier = HeartnetworkIdentifier(
                        location.id.id,
                        device.id,
                    ),
                    locationName = location.name,
                )
            }
        }.toImmutableList()
        return x
    }

    private fun mapNetworkConfigurationResultToNetworkState(
        networkConfiguration: Result<NetworkConfiguration>?,
    ): NetworkState {
        return when (networkConfiguration) {
            is Result.Error -> NetworkState.Offline
            Result.Loading -> NetworkState.Loading
            null -> NetworkState.Unknown
            is Result.Success -> {
                when (val networkConfig = networkConfiguration.value) {
                    NetworkConfiguration.Bridge -> NetworkState.Unknown
                    NetworkConfiguration.Ethernet -> NetworkState.Ethernet
                    NetworkConfiguration.Unspecified -> NetworkState.Unknown
                    is NetworkConfiguration.Wifi -> networkConfig.signalStrength?.let {
                        NetworkState.Wifi(it)
                    } ?: NetworkState.Offline
                }
            }
        }
    }
}


data class HeartnetworkSettingsDeviceListUiState(
    val deviceList: ImmutableList<DeviceListItem> = persistentListOf(),
//    val deviceList: Devices = Devices(emptyList()),
    val isLoading: Boolean = false,
//    val error: Throwable? = null
) {

}

//@Immutable
//data class Devices(val list: List<DeviceListItem>)

//@Immutable
data class DeviceListItem(
    val heartnetworkIdentifier: HeartnetworkIdentifier,
    val locationName: String,
    val networkConfiguration: NetworkState = NetworkState.Loading,
)

//@Immutable
sealed class NetworkState {
    /*@Immutable */object Loading : NetworkState()
    /*@Immutable */object Offline : NetworkState()
    /*@Immutable */object Unknown : NetworkState()
    /*@Immutable */object Ethernet : NetworkState()
    /*@Immutable */data class Wifi(val signalStrength: Int) : NetworkState()
}

private const val StopTimeoutMillis: Long = 5000

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
 *
 * When the UI stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the UI stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 * save resources when the app is in the background but let users switch between apps quickly.
 */
private val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)

fun <T, R> ViewModel.produceUiState(
    flow: Flow<T>,
    initialValue: R,
    scope: CoroutineScope = viewModelScope,
    started: SharingStarted = WhileUiSubscribed,
    uiStateProducerBlock: (T) -> R
): StateFlow<R> = flow.map(uiStateProducerBlock)
    .stateIn(
        scope = scope,
        started = started,
        initialValue = initialValue
    )

fun <T1, T2, R> ViewModel.produceUiState(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    initialValue: R,
    scope: CoroutineScope = viewModelScope,
    started: SharingStarted = WhileUiSubscribed,
    uiStateProducerBlock: (T1, T2) -> R
): StateFlow<R> = combine(flow, flow2, uiStateProducerBlock)
    .stateIn(
        scope = scope,
        started = started,
        initialValue = initialValue
    )