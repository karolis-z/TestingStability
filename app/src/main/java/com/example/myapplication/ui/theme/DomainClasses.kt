package com.example.myapplication.ui.theme

import java.io.Serializable


data class Location(
    val id: LocationId,
    val name: String,
    val personCount: Int?,
    val areaInMeters: Int,
    val primaryHeating: HeatingType?,
    val secondaryHeating: HeatingType?,
    val houseType: HouseType,
    val isDefault: Boolean,
    val address: Address,
    val devices: List<Device>,
) {

    fun getFullAddressString(): String {
        val houseNumber = if (address.houseNumber.isNullOrBlank()) "" else address.houseNumber
        val postalCode = address.postalCode
        val city = if (address.city.isNullOrBlank()) "" else address.city
        return "${address.streetName} $houseNumber, $postalCode $city"
            .trim()
            .replace("\\s+".toRegex(), " ")
            .replace("\\s+,".toRegex(), ",")
    }
}

@JvmInline
value class LocationId(val id: String): Serializable {
    init {
        if (id.isBlank()) throw IllegalArgumentException("Location id can not be bland")
    }
}


data class Device(
    val id: String, // TODO DeviceId value class
    val name: String,
    val type: DeviceType,
    val unit: DeviceUnit,
    val isProducing: Boolean,
    val subscriptions: List<Subscription>,
    val locationId: LocationId,
)

enum class DeviceUnit {
    NONE,
    LITER,
    CUBIC_METER,
    KWH,
    MWH,
}

enum class DeviceType {
    NONE,
    WATER,
    DISTRICT_HEATING,
    GAS,
    ELECTRICITY,
}

@JvmInline
value class DeviceId(val id: String): Serializable

data class Subscription(
    val id: String,
    val provider: SubscriptionProvider,
    val isActive: Boolean,
)

const val SUBSCRIPTION_TEMPORARY_ID_PREFIX = "temp_id_"

@JvmInline
value class SubscriptionId(val value: String) {
    fun isTemporaryIdForOnboarding(): Boolean {
        return SubscriptionId.isTemporaryIdForOnboarding(value)
    }

    companion object {
        fun isTemporaryIdForOnboarding(value: String): Boolean {
            return value.startsWith(SUBSCRIPTION_TEMPORARY_ID_PREFIX)
        }
    }
}


enum class SubscriptionProvider {
    MANUAL,
    NOTHOME_UNGRID,
}

enum class HeatingType {
    NONE,
    WOOD_STOVE,
    ELECTRICITY,
    DISTRICT_HEATING,
    GAS,
    OIL,
    PELLETS,
    HEAT_PUMP,
    LOCAL_HEAT_DISTRIBUTION,
    GEOTHERMAL_HEATING,
}

enum class HouseType {
    UNKNOWN,
    APARTMENT,
    HOUSE,
    SUMMER_HOUSE,
}

data class Address(
    val id: String,
    val streetName : String,
    val houseNumber : String?,
    val floor : String?,
    val door : String?,
    val city : String?,
    val postalCode : Int,
    val longitude : Double,
    val latitude : Double,
)

