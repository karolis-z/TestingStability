package com.example.myapplication.usecases

import com.example.myapplication.ui.theme.Address
import com.example.myapplication.ui.theme.Device
import com.example.myapplication.ui.theme.DeviceType
import com.example.myapplication.ui.theme.DeviceUnit
import com.example.myapplication.ui.theme.HeatingType
import com.example.myapplication.ui.theme.HouseType
import com.example.myapplication.ui.theme.Location
import com.example.myapplication.ui.theme.LocationId
import com.example.myapplication.ui.theme.Subscription
import com.example.myapplication.ui.theme.SubscriptionProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLocationsWithHeartnetworkDevicesOnlyUseCase @Inject constructor() {
    operator fun invoke(): Flow<List<Location>> {
        return flow {
            delay(2000)
            emit(listOf(
                location1,
                location2,
                location3,
            ))
        }
    }
}


private val address1 = Address(
    id = "Address1",
    streetName = "Street1",
    houseNumber = null,
    floor = null,
    door = null,
    city = null,
    postalCode = 9429,
    longitude = 4.5,
    latitude = 6.7
)
private val address2 = Address(
    id = "Address2",
    streetName = "Street2",
    houseNumber = null,
    floor = null,
    door = null,
    city = null,
    postalCode = 9429,
    longitude = 4.5,
    latitude = 6.7
)
private val address3 = Address(
    id = "Address3",
    streetName = "Street3",
    houseNumber = null,
    floor = null,
    door = null,
    city = null,
    postalCode = 9429,
    longitude = 4.5,
    latitude = 6.7
)

val device1 = Device(
    id = "device1",
    name = "Heartnetwork1",
    type = DeviceType.ELECTRICITY,
    unit = DeviceUnit.KWH,
    isProducing = false,
    subscriptions = listOf(Subscription("subscription1", SubscriptionProvider.NOTHOME_UNGRID, true)),
    locationId = LocationId(id = "locationId1")
)
val device2 = Device(
    id = "device2",
    name = "Heartnetwork2",
    type = DeviceType.ELECTRICITY,
    unit = DeviceUnit.KWH,
    isProducing = false,
    subscriptions = listOf(Subscription("subscription2", SubscriptionProvider.NOTHOME_UNGRID, true)),
    locationId = LocationId(id = "locationId2")

)
val device3 = Device(
    id = "device1",
    name = "Heartnetwork3",
    type = DeviceType.ELECTRICITY,
    unit = DeviceUnit.KWH,
    isProducing = false,
    subscriptions = listOf(Subscription("subscription3", SubscriptionProvider.NOTHOME_UNGRID, true)),
    locationId = LocationId(id = "locationId3")

)

private val location1 = Location(
    id = LocationId(id = "locationId1"),
    name = "Location 1",
    isDefault = false,
    devices = listOf(device1),
    personCount = 2,
    areaInMeters = 56,
    primaryHeating = HeatingType.HEAT_PUMP,
    secondaryHeating = HeatingType.HEAT_PUMP,
    houseType = HouseType.HOUSE,
    address = address1,
)

private val location2 = Location(
    id = LocationId(id = "locationId2"),
    name = "Location 2",
    isDefault = false,
    devices = listOf(device2),
    personCount = 2,
    areaInMeters = 56,
    primaryHeating = HeatingType.GEOTHERMAL_HEATING,
    secondaryHeating = HeatingType.ELECTRICITY,
    houseType = HouseType.HOUSE,
    address = address2,
)

private val location3 = Location(
    id = LocationId(id = "locationId3"),
    name = "Location 3",
    isDefault = false,
    devices = listOf(device3),
    personCount = 2,
    areaInMeters = 56,
    primaryHeating = HeatingType.GEOTHERMAL_HEATING,
    secondaryHeating = HeatingType.ELECTRICITY,
    houseType = HouseType.HOUSE,
    address = address3,
)


