package com.example.myapplication.usecases

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random


class GetNetworkStatusForDevicesUseCase @Inject constructor(){
    operator fun invoke(
        identifier: HeartnetworkIdentifier,
        delayInSeconds: Int,
    ): Flow<NetworkConfiguration> {
        return flow {
            val delayLength = delayInSeconds.times(1000).toLong()
            delay(delayLength)
            val result = Random.nextInt(1, 4)
            emit(
                when (result) {
                    1 -> NetworkConfiguration.Ethernet
                    2 -> NetworkConfiguration.Wifi("My network", 100)
                    3 -> NetworkConfiguration.Ethernet
                    else -> NetworkConfiguration.Wifi("My network", 80)
                }
            )
        }
    }

    data class HeartnetworkIdentifier(
        val locationId: String,
        val deviceId: String,
    )
}

sealed class NetworkConfiguration {
    object Unspecified : NetworkConfiguration()
    object Ethernet : NetworkConfiguration()
    data class Wifi(
        val ssid: String?,
        val signalStrength: Int?,
    ) : NetworkConfiguration()
    object Bridge : NetworkConfiguration()
}
